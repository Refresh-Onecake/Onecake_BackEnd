package refresh.onecake.member.application

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import refresh.onecake.response.adapter.api.ForbiddenException
import refresh.onecake.jwt.TokenProvider
import refresh.onecake.member.adapter.infra.dto.*
import refresh.onecake.member.domain.Consumer
import refresh.onecake.member.domain.ConsumerRepository
import refresh.onecake.member.domain.Member
import refresh.onecake.member.domain.MemberRepository
import refresh.onecake.member.domain.MemberType
import refresh.onecake.member.domain.Seller
import refresh.onecake.member.domain.SellerRepository
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.store.domain.StoreRepository
import java.util.concurrent.TimeUnit
import javax.transaction.Transactional


@Service
class LoginService(
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val sellerRepository: SellerRepository,
    private val consumerRepository: ConsumerRepository,
    private val storeRepository: StoreRepository
) {
    val REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7

    @Transactional
    fun signup(signUpRequestDto: SignUpRequestDto): SignUpResponseDto {
        if (memberRepository.existsByUserId(signUpRequestDto.userId)) {
            return SignUpResponseDto(false, "중복된 아이디 입니다.")
        }
        val member = memberRepository.save(
            Member(
                userId = signUpRequestDto.userId,
                userName = signUpRequestDto.name,
                password = passwordEncoder.encode(signUpRequestDto.password),
                phoneNumber = signUpRequestDto.phoneNumber,
                memberType = signUpRequestDto.memberType,
                profileImg = null,
                isActivated = true,
                fcmToken = ""
            )
        )
        giveRole(member, signUpRequestDto.memberType)
        return SignUpResponseDto(true, "회원가입을 성공했습니다.")
    }

    fun giveRole(member: Member, role: MemberType) {

        if (role == MemberType.CONSUMER) {
            var consumer = Consumer(
                id = member.id
            )
            consumerRepository.save(consumer)
        } else {
            var seller = Seller(
                id = member.id,
                store = null
            )
            sellerRepository.save(seller)
        }
    }

    @Transactional
    fun login(loginRequestDto: LoginRequestDto): TokenRoleDto {

        val member = memberRepository.findByUserId(loginRequestDto.userId)

        if (!member.isActivated) {
            throw ForbiddenException("탈퇴한 회원입니다.")
        }

        val authenticationToken = UsernamePasswordAuthenticationToken(loginRequestDto.userId, loginRequestDto.password)

        val authentication: Authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        val tokenDto: TokenDto = tokenProvider.generateTokenDto(authentication)


        val storeId = if (member.memberType == MemberType.SELLER) {
            if (storeRepository.existsById(member.id)) {
                member.id
            } else {
                0
            }
        } else {
            -1
        }

        redisTemplate.opsForValue().set(loginRequestDto.userId,
                                        loginRequestDto.fcmToken,
                                        REFRESH_TOKEN_EXPIRE_TIME.toLong(),
                                        TimeUnit.MILLISECONDS)

        val tokenRoleDto = TokenRoleDto(
            grantType = tokenDto.grantType,
            accessToken = tokenDto.accessToken,
            refreshToken = tokenDto.refreshToken,
            accessTokenExpiresIn = tokenDto.accessTokenExpiresIn,
            role = member.memberType,
            storeId = storeId
        )

        redisTemplate.opsForValue()
            .set(
                "RT:" + authentication.name,
                tokenRoleDto.refreshToken,
                REFRESH_TOKEN_EXPIRE_TIME.toLong(),
                TimeUnit.MILLISECONDS
            )

        return tokenRoleDto
    }

    @Transactional
    fun reissue(tokenRequestDto: TokenRequestDto): TokenDto? {

        if (!tokenProvider.validateToken(tokenRequestDto.refreshToken)) {
            return null
        }

        val authentication = tokenProvider.getAuthentication(tokenRequestDto.accessToken)

        val refreshToken: Any? = redisTemplate.opsForValue().get("RT:" + authentication.name)
        if (refreshToken == null || refreshToken != tokenRequestDto.refreshToken) {
            return null
        }

        val tokenDto = tokenProvider.generateTokenDto(authentication)
//        val tokenRoleDto = TokenRoleDto(grantType = tokenDto.grantType, accessToken = tokenDto.accessToken, refreshToken = tokenDto.refreshToken, accessTokenExpiresIn = tokenDto.accessTokenExpiresIn, role = memberRepository.getByUserId(loginRequestDto.userId).memberType)

        redisTemplate.delete("RT:" + authentication.name)
        redisTemplate.opsForValue().set(
            "RT:" + authentication.name,
            tokenDto.refreshToken,
            REFRESH_TOKEN_EXPIRE_TIME.toLong(),
            TimeUnit.MILLISECONDS
        )

        return tokenDto
    }

    fun logout(): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        redisTemplate.opsForValue().set("RT:$id", "", 1, TimeUnit.MILLISECONDS)
        redisTemplate.opsForValue().set(memberRepository.findMemberById(id).userId, "", 1, TimeUnit.MILLISECONDS)
        return DefaultResponseDto(true, "로그아웃 되었습니다.")
    }

    /**
     * 연락처로 userId 검색하는 Function
     * @author 메이슨
     * @param idSearchRequestDto phoneNumber
     * @return userId
     * @exception ForbiddenException 해당 연락처가 존재하지 않는 경우 / 탈퇴한 회원일 경우
     */
    @Transactional
    fun searchUserId(idSearchRequestDto: UserIdSearchRequestDto): UserIdSearchResponseDto {

        val member: Member;

        try {
            member = memberRepository.findByPhoneNumber(idSearchRequestDto.phoneNumber);
        } catch (e: EmptyResultDataAccessException) {
            throw ForbiddenException("회원이 존재하지 않습니다")
        }

        if (!member.isActivated) {
            throw ForbiddenException("탈퇴한 회원입니다.")
        }

        return UserIdSearchResponseDto(member.userId)
    }

    /**
     * userId로 password를 재설정하는 Function
     * @author 메이슨
     * @param PasswordChangeRequestDto userId, 재설정할 password
     * @exception ForbiddenException 해당 회원이 존재하지 않는 경우 / 탈퇴한 회원일 경우 / 재설정할 암호가 비어있는 경우
     */
    @Transactional
    fun changePassword(passwordChangeRequestDto: PasswordChangeRequestDto): DefaultResponseDto {
        val member: Member;

        try {
            member = memberRepository.getByUserId(passwordChangeRequestDto.userId);
        } catch (e: EmptyResultDataAccessException) {
            throw ForbiddenException("회원이 존재하지 않습니다")
        }
        if (!member.isActivated) {
            throw ForbiddenException("탈퇴한 회원입니다.")
        }
        if (passwordChangeRequestDto.password.isBlank()) {
            throw ForbiddenException("암호가 비어있습니다")
        }

        member.password = passwordEncoder.encode(passwordChangeRequestDto.password)
        memberRepository.save(member)

        return DefaultResponseDto(true, "암호 수정이 완료됐습니다.")
    }
}