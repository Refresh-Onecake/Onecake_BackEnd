package refresh.onecake.member.adapter.infra.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import refresh.onecake.member.domain.MemberType

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class SignUpRequestDto(
    val name: String,
    val userId: String,
    val password: String,
    val phoneNumber: String,
    val memberType: MemberType
)