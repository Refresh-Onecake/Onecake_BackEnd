package refresh.onecake.member.adapter.infra.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class PasswordChangeRequestDto(

    val userId: String,
    val password: String
)