package refresh.onecake.member.adapter.infra.jwt

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.ObjectUtils
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest


class JwtAuthenticationFilter(
    private val tokenProvider:TokenProvider,
    private val redisTemplate: RedisTemplate<Any, Any>
): GenericFilterBean() {

    companion object{
        const val AUTHORIZATION_HEADER = "Authorization"
        const val BEARER_TYPE = "Bearer"
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        // 1. Request Header 에서 JWT 토큰 추출
        // 1. Request Header 에서 JWT 토큰 추출
        val token: String? = resolveToken(request as HttpServletRequest)

        // 2. validateToken 으로 토큰 유효성 검사

        // 2. validateToken 으로 토큰 유효성 검사
        if (token != null && tokenProvider.validateToken(token)) {
            // (추가) Redis 에 해당 accessToken logout 여부 확인
            val isLogout = redisTemplate.opsForValue()[token] as String?
            if (ObjectUtils.isEmpty(isLogout)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                val authentication: Authentication = tokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }
        chain.doFilter(request, response)
    }

    // Request Header 에서 토큰 정보 추출
    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            bearerToken.substring(7)
        } else null
    }
}