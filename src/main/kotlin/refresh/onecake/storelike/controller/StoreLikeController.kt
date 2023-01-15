package refresh.onecake.storelike.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import refresh.onecake.common.response.DefaultResponseDto
import refresh.onecake.common.response.ApiResponse
import refresh.onecake.storelike.dto.LikedStoreWithReviewNum
import refresh.onecake.storelike.service.StoreLikeService

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

    @Operation(summary = "소비자 - 마이페이지 - 좋아요 누른 가게 ")
    @GetMapping("consumer/mypage/like/store")
    fun getLikedStoreList(): ResponseEntity<List<LikedStoreWithReviewNum>> {
        return ApiResponse.success(
            HttpStatus.OK,
            storeLikeService.getLikedStoreList()
        )
    }
}