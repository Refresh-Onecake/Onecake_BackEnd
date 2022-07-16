package refresh.onecake.member.adapter.api.dto

import refresh.onecake.member.domain.member.MemberType

data class TokenRoleDto(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long,
    val role: MemberType?,
//    val storeId: Long
)
