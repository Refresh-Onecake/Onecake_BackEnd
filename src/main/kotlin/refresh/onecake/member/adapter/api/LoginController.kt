package refresh.onecake.member.adapter.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import refresh.onecake.member.adapter.api.dto.LoginRequestDto
import refresh.onecake.member.adapter.api.dto.SignUpRequestDto
import refresh.onecake.member.adapter.api.dto.SignUpResponseDto
import refresh.onecake.member.adapter.api.dto.TokenDto
import refresh.onecake.member.application.LoginService

@RequestMapping("/api/v1/auth")
@RestController
class LoginController (
    private val loginService: LoginService
){

    @PostMapping("/signup")
    fun signup(@RequestBody signUpRequestDto: SignUpRequestDto): ResponseEntity<SignUpResponseDto> {
        return ApiResponse.success(HttpStatus.OK, loginService.signup(signUpRequestDto))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<TokenDto> {
        return ApiResponse.success(HttpStatus.OK,  loginService.login(loginRequestDto))
    }
}