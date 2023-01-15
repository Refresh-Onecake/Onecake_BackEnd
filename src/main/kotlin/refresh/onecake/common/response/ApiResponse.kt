package refresh.onecake.common.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ApiResponse {
    companion object {
        fun <T> success(code: HttpStatus, t: T): ResponseEntity<T> {
            return ResponseEntity.status(code.value()).body(t)
        }
    }
}