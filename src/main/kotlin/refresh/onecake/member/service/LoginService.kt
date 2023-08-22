package refresh.onecake.member.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import refresh.onecake.common.response.ForbiddenException
import refresh.onecake.jwt.TokenProvider
import refresh.onecake.member.dto.*
import refresh.onecake.member.entity.Consumer
import refresh.onecake.member.repository.ConsumerRepository
import refresh.onecake.member.entity.Member
import refresh.onecake.member.repository.MemberRepository
import refresh.onecake.member.entity.MemberType
import refresh.onecake.member.entity.Seller
import refresh.onecake.member.repository.SellerRepository
import refresh.onecake.common.response.DefaultResponseDto
import refresh.onecake.store.repository.StoreRepository
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

//        redisTemplate.opsForValue().set(loginRequestDto.userId,
//                                        loginRequestDto.fcmToken,
//                                        REFRESH_TOKEN_EXPIRE_TIME.toLong(),
//                                        TimeUnit.MILLISECONDS)

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
}