package refresh.onecake.member.adapter.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.member.adapter.api.dto.ApplyMenuDto
import refresh.onecake.member.adapter.api.dto.ApplyStoreRequestDto
import refresh.onecake.member.adapter.api.dto.DefaultResponseDto
import refresh.onecake.member.adapter.api.dto.StoreMenuListDto
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

    @ApiOperation(value = "메뉴 등록")
    @PostMapping("/store/menu")
    fun registerMenu(@RequestBody applyMenuDto: ApplyMenuDto): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.registerMenu(applyMenuDto))
    }

    @ApiOperation(value = "메뉴 등록")
    @GetMapping("/store/menu")
    fun getMenus(): ResponseEntity<List<StoreMenuListDto>> {
        return ApiResponse.success(HttpStatus.OK, sellerService.getMenus())
    }

}