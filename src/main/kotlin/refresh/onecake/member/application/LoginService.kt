package refresh.onecake.member.application

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import refresh.onecake.member.adapter.api.dto.*
import refresh.onecake.member.adapter.infra.jwt.TokenProvider
import refresh.onecake.member.domain.member.Member
import refresh.onecake.member.domain.member.MemberRepository
import java.util.concurrent.TimeUnit
import javax.transaction.Transactional


@Service
class LoginService (
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
    private val redisTemplate: RedisTemplate<Any, Any>
){

    @Transactional
    fun signup(signUpRequestDto: SignUpRequestDto): SignUpResponseDto {
        if (memberRepository.existsByUserId(signUpRequestDto.userId)) {
            return SignUpResponseDto(false, "중복된 아이디 입니다.")
        }
        var member = Member(
            userId = signUpRequestDto.userId,
            userName = signUpRequestDto.name,
            password = passwordEncoder.encode(signUpRequestDto.password),
            phoneNumber = signUpRequestDto.phoneNumber,
            memberType = signUpRequestDto.memberType,
            profileImg = null
        )
        memberRepository.save(member)
        return SignUpResponseDto(true, "회원가입을 성공했습니다.")
    }

    @Transactional
    fun login(loginRequestDto: LoginRequestDto): TokenDto {

        val authenticationToken = UsernamePasswordAuthenticationToken(loginRequestDto.userId, loginRequestDto.password)

        val authentication: Authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        val tokenDto: TokenDto = tokenProvider.generateTokenDto(authentication)

        redisTemplate.opsForValue()
            .set("RT:" + authentication.name, tokenDto.refreshToken, (1000 * 60 * 60 * 24 * 7).toLong(), TimeUnit.MILLISECONDS)

        return tokenDto
    }

//    @Transactional
//    fun reissue(tokenRequestDto: TokenRequestDto): TokenDto? {
//
//        if (!tokenProvider.validateToken(tokenRequestDto.refreshToken)) {
//            DefaultResponseDto(false, "Refresh Token 이 유효하지 않습니다.")
//        }
//
//        // 2. Access Token 에서 Member ID 가져오기
//        val authentication = tokenProvider.getAuthentication(tokenRequestDto.accessToken)
//
//        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
//        val refreshToken: RefreshToken = refreshTokenRepository.findByKey(authentication.name)
//            .orElseThrow { RuntimeException("로그아웃 된 사용자입니다.") }
//
//        // 4. Refresh Token 일치하는지 검사
//        if (!refreshToken.getValue().equals(tokenRequestDto.refreshToken)) {
//            throw RuntimeException("토큰의 유저 정보가 일치하지 않습니다.")
//        }
//
//        // 5. 새로운 토큰 생성
//        val tokenDto = tokenProvider.generateTokenDto(authentication)
//
//        // 6. 저장소 정보 업데이트
//        val newRefreshToken: RefreshToken = refreshToken.updateValue(tokenDto.refreshToken)
//        refreshTokenRepository.save(newRefreshToken)
//
//        // 토큰 발급
//        return tokenDto
//    }
}