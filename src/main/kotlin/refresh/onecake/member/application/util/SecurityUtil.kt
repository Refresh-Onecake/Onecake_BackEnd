package refresh.onecake.member.application.util

import org.springframework.security.core.context.SecurityContextHolder


class SecurityUtil() {
    // SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    companion object{
        fun getCurrentMemberId(): Long {
            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication == null || authentication.name == null) {
                throw RuntimeException("Security Context 에 인증 정보가 없습니다.")
            }
            return authentication.name.toLong()
        }
    }
}
