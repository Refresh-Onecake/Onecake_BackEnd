package refresh.onecake.member.application

import org.modelmapper.ModelMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import refresh.onecake.member.adapter.api.dto.LoginRequestDto
import refresh.onecake.member.adapter.api.dto.SignUpRequestDto
import refresh.onecake.member.adapter.api.dto.SignUpResponseDto
import refresh.onecake.member.adapter.api.dto.TokenDto
import refresh.onecake.member.adapter.infra.jwt.TokenProvider
import refresh.onecake.member.domain.exception.DuplicatedUserIdException
import refresh.onecake.member.domain.member.Member
import refresh.onecake.member.domain.member.MemberRepository

@Service
class LoginService (
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
    private val redisTemplate: RedisTemplate<Any, Any>,
    private val modelMapper: ModelMapper
){

    fun signup(signUpRequestDto: SignUpRequestDto): SignUpResponseDto {
        if (memberRepository.existsByUserId(signUpRequestDto.userId)) {
            throw DuplicatedUserIdException("이미 가입되어 있는 유저입니다.")
        }
        println("1")
        var member:Member = Member(
            userId = signUpRequestDto.userId,
            userName = signUpRequestDto.name,
            password = passwordEncoder.encode(signUpRequestDto.password),
            phoneNumber = signUpRequestDto.phoneNumber,
            memberType = signUpRequestDto.memberType,
            profileImg = null
        )
        println("2")
//        signUpRequestDto.password
//        passwordEncoder.encode(signUpRequestDto.password)
//        lateinit var member:Member
//        modelMapper.map(member, SignUpRequestDto::class.java)
        memberRepository.save(member)
        println("3")
        val signUpResponseDto = SignUpResponseDto(true)
        return signUpResponseDto

    }

    fun login(loginRequestDto: LoginRequestDto): TokenDto {
        println("0")
        val authenticationToken = UsernamePasswordAuthenticationToken(loginRequestDto.userId, loginRequestDto.password)
        println("1")
        val authentication: Authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        println("2")
        val tokenDto: TokenDto = tokenProvider.generateTokenDto(authentication)
        println("3")
        redisTemplate.opsForValue()
            .set("RT:" + authentication.name, tokenDto.refreshToken)
        println("4")
        return tokenDto

    }
}