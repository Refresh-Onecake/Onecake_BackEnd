package refresh.onecake.imagelike.adapter.infra.api

import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.imagelike.application.ImageLikeService
import refresh.onecake.response.adapter.api.ApiResponse

@RestController
@RequestMapping("/api/v1/")
@Api(value = "Image Like", description = "메뉴의 이미지에 대한 좋아요 기능을 하는 controller")
class ImageLikeController (
    private val imageLikeService: ImageLikeService
){

    @Operation(summary = "특정 이미지 좋아요하기")
    @PostMapping("/seller/store/menu/{menuId}/image/{imageId}/like")
    fun postImageLike(@PathVariable menuId: Long, @PathVariable imageId: Long): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            imageLikeService.postImageLike(menuId, imageId)
        )
    }
}