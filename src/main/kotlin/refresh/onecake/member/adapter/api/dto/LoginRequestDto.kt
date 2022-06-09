package refresh.onecake.member.adapter.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import refresh.onecake.member.domain.member.Member
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class LoginRequestDto(

    @field:NotEmpty(message="아이디를 입력하세요.")
    @field:NotBlank
    val userId: String,

    @field:NotEmpty
    @field:NotBlank
    val password: String
)