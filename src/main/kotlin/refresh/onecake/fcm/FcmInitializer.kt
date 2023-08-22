//package refresh.onecake.fcm
//
//import com.google.auth.oauth2.GoogleCredentials
//import com.google.firebase.FirebaseApp
//import com.google.firebase.FirebaseOptions
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.core.io.ClassPathResource
//import org.springframework.stereotype.Component
//import javax.annotation.PostConstruct
//
//@Component
//class FcmInitializer {
//
//    @Value("\${fcm.certification}")
//    private val googleApplicationCredentials: String = ""
//
//    @PostConstruct
//    fun initialize() {
//        val resource = ClassPathResource(googleApplicationCredentials)
//        resource.inputStream.use { `is` ->
//            val options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(`is`))
//                .build()
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options)
//            }
//        }
//    }
//}