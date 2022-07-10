package refresh.onecake.member.adapter.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.member.adapter.api.dto.*
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

    @ApiOperation(value = "메뉴 보기")
    @GetMapping("/store/menu")
    fun getMenus(): ResponseEntity<List<StoreMenuListDto>> {
        return ApiResponse.success(HttpStatus.OK, sellerService.getMenus())
    }

    @ApiOperation(value = "휴일 지정")
    @PostMapping("/store/dayOff")
    fun setDayOff(@RequestBody dayAndName: DayAndName): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.setDayOff(dayAndName))
    }

    @ApiOperation(value = "휴일 가져오기")
    @GetMapping("/store/dayOff/{dayOff}")
    fun getDayOff(@PathVariable dayOff: String): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.getDayOff(dayOff))
    }

    @ApiOperation(value = "특정 날짜의 주문들 가져오기")
    @GetMapping("/store/order/{day}")
    fun getSpecificDatesOrder(@PathVariable day: String): ResponseEntity<OrdersClassifiedByState> {
        return ApiResponse.success(HttpStatus.OK, sellerService.getSpecificDatesOrder(day))
    }

    @ApiOperation(value = "특정 주문서 가져오기")
    @GetMapping("/store/order/form/{orderId}")
    fun getSpecificOrder(@PathVariable orderId: Long): ResponseEntity<SpecificOrderForm> {
        return ApiResponse.success(HttpStatus.OK, sellerService.getSpecificOrder(orderId))
    }

    @ApiOperation(value = "특정 주문서에 메모 저장하기")
    @PostMapping("/store/order/form/{orderId}/memo")
    fun postMemo(@PathVariable orderId: Long, @RequestBody memo: Memo): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.postMemo(orderId, memo))
    }

    @ApiOperation(value = "주문 상태 다음 단계로 변경")
    @PostMapping("/store/order/form/{orderId}/state")
    fun orderStateToAccepted(@PathVariable orderId: Long): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.changeOrderState(orderId))
    }

    @ApiOperation(value = "주문 취소로 상태 변경")
    @PostMapping("/store/order/form/{orderId}/state/cancel")
    fun orderStateToCanceled(@PathVariable orderId: Long, @RequestBody cancelReason: CancelReason): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.orderStateToCanceled(orderId, cancelReason))
    }

//    @ApiOperation(value = "케이크 제작하기로 상태 변경")
//    @PostMapping("/store/order/form/{orderId}/state/accepted")
//    fun orderStateToMaking(@PathVariable orderId: Long): ResponseEntity<DefaultResponseDto> {
//        return ApiResponse.success(HttpStatus.OK, sellerService.orderStateToMaking(orderId))
//    }
//
//    @ApiOperation(value = "픽업 완료하기로 상태 변경")
//    @PostMapping("/store/order/form/{orderId}/state/accepted")
//    fun orderStateToCompleted(@PathVariable orderId: Long): ResponseEntity<DefaultResponseDto> {
//        return ApiResponse.success(HttpStatus.OK, sellerService.orderStateToCompleted(orderId))
//    }
//
//    @ApiOperation(value = "다시 진행하기로 상태 변경")
//    @PostMapping("/store/order/form/{orderId}/state/accepted")
//    fun orderStateToReceived(@PathVariable orderId: Long): ResponseEntity<DefaultResponseDto> {
//        return ApiResponse.success(HttpStatus.OK, sellerService.orderStateToReceived(orderId))
//    }

}