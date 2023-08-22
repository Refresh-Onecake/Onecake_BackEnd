//package refresh.onecake.fcm
//
//import com.google.firebase.messaging.FirebaseMessaging
//import com.google.firebase.messaging.Message
//import org.springframework.stereotype.Service
//
//
//@Service
//class FcmService(
//    private val fcmToken: FcmToken
//){
//
//    fun sendPickUpMessage(userId: String, userName: String) {
//        if (!fcmToken.hasKey(userId)) {
//            return
//        }
//        val token: String? = fcmToken.getToken(userId)
//        val message: Message = Message.builder()
//            .putData("title", "케이크 픽업 알림")
//            .putData("content", "${userName}님의 케이크가 픽업 준비중이에요!")
//            .setToken(token)
//            .build()
//        send(message)
//    }
//
//    fun sendCakeOrderReceivedMessage(userId: String, userName: String, menuName: String) {
//        if (!fcmToken.hasKey(userId)) {
//            return
//        }
//        val token: String? = fcmToken.getToken(userId)
//        val message: Message = Message.builder()
//            .putData("title", "신규 주문 알림")
//            .putData("content", "${userName}님이 ${menuName}를 주문했어요!")
//            .setToken(token)
//            .build()
//        send(message)
//    }
//
//    fun send(message: Message?) {
//        FirebaseMessaging.getInstance().sendAsync(message)
//    }
//
//    // ...
//}