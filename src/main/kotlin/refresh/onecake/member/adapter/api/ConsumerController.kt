package refresh.onecake.member.adapter.api

import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import refresh.onecake.member.adapter.api.dto.*
import refresh.onecake.member.application.ConsumerService

@RestController
@RequestMapping("/api/v1/consumer/")
class ConsumerController (
    private val consumerService: ConsumerService
){

    @ApiOperation(value = "소비자 - 상세 가게 페이지 - 윗부분 ")
    @GetMapping("stores/{storeId}/mainInfo")
    fun storeMainInfo(@PathVariable storeId:Long): ResponseEntity<StoreMainInfoDto> {
        return ApiResponse.success(HttpStatus.OK, consumerService.storeMainInfo(storeId))
    }

    @ApiOperation(value = "소비자 - 상세 가게 페이지 - 좋아요/취소 ")
    @PostMapping("stores/{storeId}/like")
    fun pushStoreLike(@PathVariable storeId: Long): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, consumerService.pushStoreLike(storeId))
    }

    @ApiOperation(value = "소비자 - 상세 가게 페이지 - 아랫부분 - 메뉴 ")
    @GetMapping("stores/{storeId}/menuList")
    fun storeMenuList(@PathVariable storeId:Long): ResponseEntity<List<StoreMenuListDto>?> {
        return ApiResponse.success(HttpStatus.OK, consumerService.storeMenuList(storeId))
    }

    @ApiOperation(value = "소비자 - 상세 가게 페이지 - 아랫부분 - 가게정보 ")
    @GetMapping("stores/{storeId}/storeInfo")
    fun getStoreInformation(@PathVariable storeId: Long): ResponseEntity<StoreDetailInfoDto> {
        return ApiResponse.success(HttpStatus.OK, consumerService.getStoreInformation(storeId))
    }

    @ApiOperation(value = "소비자 - 주문하기 - 케잌사이즈들 ")
    @GetMapping("stores/{storeId}/order/cakesize")
    fun getCakesSize(@PathVariable storeId: Long): ResponseEntity<StoreNameAndCakeSizesDto> {
        return ApiResponse.success(HttpStatus.OK, consumerService.getCakesSize(storeId))
    }

    @ApiOperation(value = "소비자 - 특정 메뉴의 이미지들")
    @GetMapping("stores/{storeId}/order/cakes/{menuId}")
    fun getAllImagesOfSpecificMenu(@PathVariable storeId: Long, @PathVariable menuId: Long): ResponseEntity<MenuDescAndImages> {
        return ApiResponse.success(HttpStatus.OK, consumerService.getAllImagesOfSpecificMenu(storeId, menuId))
    }

    @ApiOperation(value = "소비자 - 주문하기 - GET 주문서 질문들 ")
    @GetMapping("stores/{storeId}/order/{menuId}")
    fun getOrderSheet(@PathVariable storeId: Long, @PathVariable menuId:Long): ResponseEntity<OrderSheetTwoTypeDto?> {
        return ApiResponse.success(HttpStatus.OK, consumerService.getOrderSheet(storeId, menuId))
    }

    @ApiOperation(value = "소비자 - 주문하기 - GET 가게의 휴무일 ")
    @GetMapping("stores/{storeId}/order/dayOff")
    fun getStoreDayOffs(@PathVariable storeId: Long): ResponseEntity<List<String>?> {
        return ApiResponse.success(HttpStatus.OK, consumerService.getStoreDayOffs(storeId))
    }

    @ApiOperation(value = "소비자 - 주문하기 - POST 주문서 질문의 대답 ")
    @PostMapping("stores/{storeId}/order/{menuId}")
    fun postOrderSheet(
        @PathVariable storeId: Long,
        @PathVariable menuId: Long,
        @RequestBody answersDto: AnswersDto
    ): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, consumerService.postOrderSheet(storeId, menuId, answersDto))
    }

    @ApiOperation(value = "소비자 - 가게 탭 (해당 지역의 모든 가게 보기) & 인기순 ")
    @GetMapping("stores/main")
    fun getAllStoreByAddress(@RequestBody addressAndFilter: AddressAndFilter): ResponseEntity<List<StoreThumbNail>?> {
        return ApiResponse.success(HttpStatus.OK, consumerService.getAllStoreByAddressAndFilter(addressAndFilter))
    }

}