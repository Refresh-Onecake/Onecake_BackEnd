package refresh.onecake.member.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import refresh.onecake.member.dto.*
import refresh.onecake.common.response.ApiResponse
import refresh.onecake.member.service.LoginService
import refresh.onecake.common.response.DefaultResponseDto

//@Tag(tags = arrayOf(LOGIN_CONTROLLER_TAG), description = "회원가입, 로그인, 토큰 재발급")
@RequestMapping("/api/v1/auth")
@RestController
class LoginController (
    private val loginService: LoginService
){
    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    fun signup(@RequestBody signUpRequestDto: SignUpRequestDto): ResponseEntity<SignUpResponseDto> {
        val signUpResponseDto: SignUpResponseDto = loginService.signup(signUpRequestDto)
        return if (signUpResponseDto.success) {
            ApiResponse.success(HttpStatus.OK, signUpResponseDto)
        } else {
            ApiResponse.success(HttpStatus.BAD_REQUEST, signUpResponseDto)
        }
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    fun login(@RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<TokenRoleDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            loginService.login(loginRequestDto)
        )
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    fun reissue(@RequestBody tokenRequestDto: TokenRequestDto): ResponseEntity<TokenDto> {
        val tokenDto: TokenDto? = loginService.reissue(tokenRequestDto)
        return if (tokenDto != null) {
            ApiResponse.success(HttpStatus.OK, tokenDto)
        } else {
            val invalidTokenDto = TokenDto("", "", "", -1L)
            ApiResponse.success(HttpStatus.BAD_REQUEST, invalidTokenDto)
        }
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    fun logout(): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, loginService.logout())
    }
}