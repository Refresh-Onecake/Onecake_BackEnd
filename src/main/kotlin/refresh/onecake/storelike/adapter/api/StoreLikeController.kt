package refresh.onecake.storelike.adapter.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.response.adapter.api.ApiResponse
import refresh.onecake.storelike.application.StoreLikeService

@RestController
@RequestMapping("/api/v1/")
class StoreLikeController (
    private val storeLikeService: StoreLikeService
){

    @Operation(summary = "소비자 - 상세 가게 페이지 - 좋아요/취소 ")
    @PostMapping("consumer/stores/{storeId}/like")
    fun pushStoreLike(@PathVariable storeId: Long): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            storeLikeService.pushStoreLike(storeId)
        )
    }
}