package refresh.onecake.member.adapter.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import refresh.onecake.member.adapter.api.dto.*
import refresh.onecake.member.application.LoginService


@RequestMapping("/api/v1/auth")
@RestController
class LoginController (
    private val loginService: LoginService
){
    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    fun signup(@RequestBody signUpRequestDto: SignUpRequestDto): ResponseEntity<SignUpResponseDto> {
        val signUpResponseDto:SignUpResponseDto = loginService.signup(signUpRequestDto)
        return if (signUpResponseDto.success) {
            ApiResponse.success(HttpStatus.OK, signUpResponseDto)
        } else {
            ApiResponse.success(HttpStatus.BAD_REQUEST, signUpResponseDto)
        }
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    fun login(@RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<TokenDto> {
        return ApiResponse.success(HttpStatus.OK,  loginService.login(loginRequestDto))
    }

    @PostMapping("/reissue")
    fun reissue(@RequestBody tokenRequestDto: TokenRequestDto): ResponseEntity<TokenDto> {
        val tokenDto:TokenDto? = loginService.reissue(tokenRequestDto)
        return if (tokenDto != null) {
            ApiResponse.success(HttpStatus.OK, tokenDto)
        } else {
            val invalidTokenDto = TokenDto("", "", "", -1L)
            ApiResponse.success(HttpStatus.BAD_REQUEST, invalidTokenDto)
        }
    }
}