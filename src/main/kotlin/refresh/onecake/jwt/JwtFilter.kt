package refresh.onecake.jwt

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtFilter
    (
    private val tokenProvider: TokenProvider
) : OncePerRequestFilter() {

    companion object{
        const val AUTHORIZATION_HEADER:String = "Authorization"
        const val BEARER_PREFIX:String = "Bearer"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
//        var jwt: String = resolveToken(request)
        val jwt = resolveToken(request)

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            var authentication:Authentication = tokenProvider.getAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)

    }


    private fun resolveToken(request: HttpServletRequest): String? {
        var bearerToken:String? = request.getHeader(AUTHORIZATION_HEADER)
        if (bearerToken != null) {
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
                return bearerToken.substring(7)
            } else {
                return null
            }
        } else {
            return null
        }
    }
}


