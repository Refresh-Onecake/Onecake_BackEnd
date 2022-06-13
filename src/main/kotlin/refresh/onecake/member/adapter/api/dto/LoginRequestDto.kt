package refresh.onecake.member.adapter.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import refresh.onecake.member.domain.member.Member
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class LoginRequestDto(

    val userId: String,
    val password: String
)