package refresh.onecake.member.adapter.infra.dto

data class TokenDto(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long
)