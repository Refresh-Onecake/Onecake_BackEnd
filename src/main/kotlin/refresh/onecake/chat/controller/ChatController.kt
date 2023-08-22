package refresh.onecake.chat.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import refresh.onecake.chat.entity.Chat
import refresh.onecake.chat.service.ChatService
import refresh.onecake.chat.service.ChatStoreInfo
import refresh.onecake.common.response.ApiResponse
import refresh.onecake.common.response.DefaultResponseDto

@RestController
@RequestMapping("/api/v1/")
class ChatController(
        val chatService: ChatService
) {

    @Operation(summary = "채팅한 가게 리스트 불러오기 ")
    @GetMapping("consumer/chat")
    fun getChats(): ResponseEntity<List<ChatStoreInfo>> {
        return ApiResponse.success(
                HttpStatus.OK,
                chatService.getChats()
        )
    }

    @Operation(summary = "채팅한 가게 리스트 불러오기 ")
    @PostMapping("consumer/chat")
    fun postChat(@RequestBody chatInfo: ChatInfo): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
                HttpStatus.OK,
                chatService.postChat(chatInfo.storeId)
        )
    }
}

data class ChatInfo(
        val storeId: Long
)