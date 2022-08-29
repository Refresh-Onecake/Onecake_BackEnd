package refresh.onecake.review.adapter.infra.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.review.adapter.infra.dto.PostReview
import refresh.onecake.orderhistory.adapter.infra.dto.ReviewAndNum
import refresh.onecake.response.adapter.api.ApiResponse
import refresh.onecake.review.application.ReviewService

@RestController
@RequestMapping("/api/v1/")
class ReviewController (
    private val reviewService: ReviewService
){

    @Operation(summary = "소비자 - 리뷰 쓰기 ")
    @PostMapping("consumer/stores/review")
    fun postReview(@RequestBody postReview: PostReview): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            reviewService.postReview(postReview)
        )
    }

    @Operation(summary = "소비자 - 가게의 리뷰 보기 ")
    @GetMapping("consumer/stores/review/{storeId}")
    fun getAllReviewsOfSpecificStore(@PathVariable storeId: Long): ResponseEntity<ReviewAndNum> {
        return ApiResponse.success(
            HttpStatus.OK,
            reviewService.getAllReviewsOfSpecificStore(storeId)
        )
    }
}