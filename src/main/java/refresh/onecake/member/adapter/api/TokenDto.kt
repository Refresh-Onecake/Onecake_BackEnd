package refresh.onecake.member.adapter.api

data class TokenDto(
    private val grantType: String,
    private val accessToken: String,
    private val refreshToken: String,
    private val accessTokenExpiresIn: Long
)