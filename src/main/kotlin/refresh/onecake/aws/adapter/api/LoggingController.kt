package refresh.onecake.aws.adapter.api

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LoggingController {

    @Value("\${logging-module.version}")
    private val version: String = ""

    @GetMapping("/")
    fun version(): String {
        return String.format("Project Version : %s", version)
    }

    @GetMapping("/health")
    fun healthCheck() : String {
        return "healthy"
    }
}