package refresh.onecake.orderhistory.adapter.infra.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import refresh.onecake.orderhistory.adapter.infra.dto.*
import refresh.onecake.orderhistory.application.OrderHistoryService
import refresh.onecake.response.adapter.api.ApiResponse
import refresh.onecake.response.adapter.dto.DefaultResponseDto

@RestController
@RequestMapping("/api/v1/")
class OrderHistoryController (
    private val orderHistoryService: OrderHistoryService
){

    @Operation(summary = "소비자 - 나의 주문 내역 보기")
    @GetMapping("consumer/orderHistory")
    fun getOrderHistorys(): ResponseEntity<List<MyOrderHistorys>> {
        return ApiResponse.success(
            HttpStatus.OK,
            orderHistoryService.getOrderHistorys()
        )
    }

    @Operation(summary = "소비자 - 특정 주문 내역 보기")
    @GetMapping("consumer/orderHistory/{orderHistoryId}")
    fun getSpecificOrderHistory(@PathVariable orderHistoryId: Long): ResponseEntity<SpecificOrderHistory> {
        return ApiResponse.success(
            HttpStatus.OK,
            orderHistoryService.getSpecificOrderHistory(orderHistoryId)
        )
    }






    @Operation(summary = "특정 날짜의 주문들 가져오기")
    @GetMapping("/seller/store/order/{day}")
    fun getSpecificDatesOrder(@PathVariable day: String): ResponseEntity<OrdersClassifiedByState> {
        return ApiResponse.success(
            HttpStatus.OK,
            orderHistoryService.getSpecificDatesOrder(day)
        )
    }

    @Operation(summary = "특정 주문서 가져오기")
    @GetMapping("/seller/store/order/form/{orderId}")
    fun getSpecificOrder(@PathVariable orderId: Long): ResponseEntity<SpecificOrderForm> {
        return ApiResponse.success(
            HttpStatus.OK,
            orderHistoryService.getSpecificOrder(orderId)
        )
    }

    @Operation(summary = "특정 주문서에 메모 저장하기")
    @PostMapping("/seller/store/order/form/{orderId}/memo")
    fun postMemo(@PathVariable orderId: Long, @RequestBody memo: Memo): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            orderHistoryService.postMemo(orderId, memo)
        )
    }

    @Operation(summary = "주문 상태 다음 단계로 변경")
    @PostMapping("/seller/store/order/form/{orderId}/state")
    fun orderStateToAccepted(@PathVariable orderId: Long): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            orderHistoryService.changeOrderState(orderId)
        )
    }

    @Operation(summary = "주문 취소로 상태 변경")
    @PostMapping("/seller/store/order/form/{orderId}/state/cancel")
    fun orderStateToCanceled(@PathVariable orderId: Long, @RequestBody cancelReason: CancelReason): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            orderHistoryService.orderStateToCanceled(orderId, cancelReason)
        )
    }

}