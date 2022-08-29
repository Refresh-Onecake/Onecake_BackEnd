package refresh.onecake.member.adapter.infra.dto

data class TokenRequestDto(
    val accessToken: String,
    val refreshToken: String
)
