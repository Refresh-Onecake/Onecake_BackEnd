package refresh.onecake.member.adapter.infra.dto

import refresh.onecake.member.domain.MemberType

data class TokenRoleDto(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long,
    val role: MemberType?,
    val storeId: Long?
)
