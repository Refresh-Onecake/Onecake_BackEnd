package refresh.onecake.menu.adapter.infra.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import refresh.onecake.menu.adapter.infra.dto.*
import refresh.onecake.menu.application.ImageService
import refresh.onecake.response.adapter.api.ApiResponse
import refresh.onecake.response.adapter.dto.DefaultResponseDto

@RestController
@RequestMapping("/api/v1/")
class ImageController (
    private val imageService: ImageService
){

    @Operation(summary = "소비자 - 특정 메뉴의 이미지들")
    @GetMapping("consumer/stores/{storeId}/order/cakes/{menuId}")
    fun getAllImagesOfSpecificMenu(@PathVariable storeId: Long, @PathVariable menuId: Long): ResponseEntity<MenuDescAndImages> {
        return ApiResponse.success(
            HttpStatus.OK,
            imageService.getAllImagesOfSpecificMenu(storeId, menuId)
        )
    }

    @Operation(summary = "소비자 - 키워드별 케이크 불러오기")
    @GetMapping("consumer/home/keyword")
    fun getKeyWordCakes(): ResponseEntity<List<KeywordImages>> {
        return ApiResponse.success(
            HttpStatus.OK,
            imageService.getKeywordCakes()
        )
    }






    @Operation(summary = "특정 메뉴의 이미지들 불러오기")
    @GetMapping("/seller/store/menu/{menuId}/image")
    fun getImagesOfSpecificMenu(@PathVariable menuId: Long): ResponseEntity<MenuImageSetting> {
        return ApiResponse.success(
            HttpStatus.OK,
            imageService.getImagesOfSpecificMenu(menuId)
        )
    }

    @Operation(summary = "특정 메뉴의 이미지 추가하기")
    @PostMapping("/seller/store/menu/{menuId}/image")
    fun postImage(
        @PathVariable menuId: Long,
        @RequestBody imageAndKeyword: ImageAndKeyword
    ): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            imageService.postImage(menuId, imageAndKeyword)
        )
    }

    @Operation(summary = "특정 이미지 불러오기")
    @GetMapping("/seller/store/menu/{menuId}/image/{imageId}")
    fun getImageDetail(@PathVariable menuId: Long, @PathVariable imageId: Long): ResponseEntity<ImageDetail>{
        return ApiResponse.success(
            HttpStatus.OK,
            imageService.getImageDetail(menuId, imageId)
        )
    }

    @Operation(summary = "특정 이미지 삭제하기")
    @DeleteMapping("/seller/store/menu/{menuId}/image/{imageId}")
    fun deleteImage(@PathVariable menuId: Long, @PathVariable imageId: Long): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            imageService.deleteImage(imageId)
        )
    }
}