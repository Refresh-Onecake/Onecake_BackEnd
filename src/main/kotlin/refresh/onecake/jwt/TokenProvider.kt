package refresh.onecake.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import refresh.onecake.member.dto.TokenDto
import java.security.Key
import java.util.*
import java.util.stream.Collectors


@Component
class TokenProvider {

    @Value("\${jwt.secret}")
    lateinit var secretKey: String

    val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        const val AUTHORITIES_KEY = "auth"
        const val BEARER_TYPE = "bearer"
        const val ACCESS_TOKEN_EXPIRE_TIME = (1000 * 60 * 60 * 24 * 365).toLong()
        const val REFRESH_TOKEN_EXPIRE_TIME = (1000 * 60 * 60 * 24 * 7).toLong()
    }

    private lateinit var key: Key

    constructor(
        @Value("\${jwt.secret}")
        secretKey:String
    ){
        var keyBytes: ByteArray = Decoders.BASE64.decode(secretKey)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateTokenDto(authentication: Authentication): TokenDto {
        var autorities: String = authentication.authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","))

        var now:Long = Date().time

        var accessTokenExpiresIn = Date(now + ACCESS_TOKEN_EXPIRE_TIME)
        var accessToken: String = Jwts.builder()
            .setSubject(authentication.name)
            .claim(AUTHORITIES_KEY, autorities)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
        var refreshToken:String = Jwts.builder()
            .setExpiration(Date(now + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()

        return TokenDto(
            grantType = BEARER_TYPE,
            accessToken = accessToken,
            accessTokenExpiresIn = accessTokenExpiresIn.time,
            refreshToken = refreshToken
        )
    }

    fun getAuthentication(accessToken: String?): Authentication {
        var claims:Claims = parseClaims(accessToken)

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw RuntimeException("권한 정보가 없는 토큰입니다.")
        }

        var authorities: Collection<GrantedAuthority?> =
            Arrays.stream(claims[AUTHORITIES_KEY].toString().split(",").toTypedArray())
                .map { role: String? ->
                    SimpleGrantedAuthority(
                        role
                    )
                }
                .collect(Collectors.toList())

        var principal:UserDetails = User(claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SecurityException) {
            logger.info("잘못된 JWT 서명입니다.")
        } catch (e: MalformedJwtException) {
            logger.info("잘못된 JWT 서명입니다.")
        } catch (e: ExpiredJwtException) {
            logger.info("만료된 JWT 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            logger.info("지원되지 않는 JWT 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            logger.info("JWT 토큰이 잘못되었습니다.")
        }
        return false
    }

    // 수정
    private fun parseClaims(accessToken: String?): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }

    fun getExpiration(accessToken: String): Long {
        // accessToken 남은 유효시간
        val expiration: Date =
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body.expiration
        // 현재 시간
        val now = Date().time
        return expiration.time - now
    }

}
