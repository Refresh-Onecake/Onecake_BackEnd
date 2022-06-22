package refresh.onecake.member.adapter.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.member.adapter.api.dto.ApplyStoreRequestDto
import refresh.onecake.member.adapter.api.dto.DefaultResponseDto
import refresh.onecake.member.application.SellerService

@Api(tags = arrayOf(SwaggerNotes.SELLER_CONTROLLER_TAG), description = "입점 신청")
@RestController
@RequestMapping("/api/v1/seller")
class SellerController (
    private val sellerService: SellerService
){

    @ApiOperation(value = "입점 신청", notes = SwaggerNotes.REGISTER_STORE_MEMO)
    @PostMapping("/store")
    fun registerStore(@RequestBody applyStoreRequestDto: ApplyStoreRequestDto): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.registerStore(applyStoreRequestDto))
    }

    @ApiOperation(value = "가게 이미지 업로드", notes = SwaggerNotes.REGISTER_STORE_MEMO)
    @PostMapping("/store/image")
    fun registerStoreImage(@RequestPart("image") image:MultipartFile): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.registerStoreImage(image))
    }
}