package refresh.onecake.member.adapter.api.dto

data class TokenRequestDto(
    val accessToken: String,
    val refreshToken: String
)
