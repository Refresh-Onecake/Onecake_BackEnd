package refresh.onecake.member.adapter.infra.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class LoginRequestDto(

    val userId: String,
    val password: String
)