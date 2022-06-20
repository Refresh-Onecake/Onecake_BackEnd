package refresh.onecake.member.application

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import refresh.onecake.member.domain.member.Member
import refresh.onecake.member.domain.member.MemberRepository
import refresh.onecake.member.domain.seller.SellerRepository
import java.util.*
import javax.transaction.Transactional


@Service
class CustomUserDetailsService(
    private val memberRepository: MemberRepository
) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(userId: String): UserDetails {
        val member:Member? = memberRepository.findByUserId(userId)
        if (member != null) {
            return createUserDetails(member)
        } else {
            throw UsernameNotFoundException("$userId -> 데이터베이스에서 찾을 수 없습니다.")
        }
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private fun createUserDetails(member: Member): UserDetails {
        val grantedAuthority: GrantedAuthority = SimpleGrantedAuthority("ROLE_USER")
        return User(
            java.lang.String.valueOf(member.id),
            member.password,
            Collections.singleton(grantedAuthority)
        )
    }
}