package refresh.onecake.member.application

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
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
    private val redisTemplate: RedisTemplate<String, Any>
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

    @Transactional
    fun reissue(tokenRequestDto: TokenRequestDto): TokenDto? {

        if (!tokenProvider.validateToken(tokenRequestDto.refreshToken)) {
            return null
        }

        val authentication = tokenProvider.getAuthentication(tokenRequestDto.accessToken)

        val refreshToken:Any? = redisTemplate.opsForValue().get("RT:" + authentication.name)
        if (refreshToken == null || refreshToken != tokenRequestDto.refreshToken) {
            return null
        }

        val tokenDto = tokenProvider.generateTokenDto(authentication)

        redisTemplate.opsForValue().getAndDelete("RT:" + authentication.name)
        redisTemplate.opsForValue().set("RT:" + authentication.name, tokenDto.refreshToken, (1000 * 60 * 60 * 24 * 7).toLong(), TimeUnit.MILLISECONDS)

        return tokenDto
    }
}