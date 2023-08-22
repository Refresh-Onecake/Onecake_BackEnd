//package refresh.onecake.fcm
//
//import org.springframework.data.redis.core.StringRedisTemplate
//import org.springframework.stereotype.Service
//import refresh.onecake.member.dto.LoginRequestDto
//
//@Service
//class FcmToken (
//    private val tokenRedisTemplate: StringRedisTemplate
//){
//    fun saveToken(loginRequest: LoginRequestDto) {
//        tokenRedisTemplate.opsForValue()[loginRequest.userId] = loginRequest.fcmToken
//    }
//
//    fun getToken(email: String): String? {
//        return tokenRedisTemplate.opsForValue()[email]
//    }
//
//    fun deleteToken(email: String) {
//        tokenRedisTemplate.delete(email)
//    }
//
//    fun hasKey(email: String): Boolean {
//        return tokenRedisTemplate.hasKey(email)
//    }
//}
