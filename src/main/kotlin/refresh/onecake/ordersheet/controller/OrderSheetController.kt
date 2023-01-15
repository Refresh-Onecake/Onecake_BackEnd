package refresh.onecake.ordersheet.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import refresh.onecake.ordersheet.dto.AnswersDto
import refresh.onecake.ordersheet.dto.OrderSheetTwoTypeDto
import refresh.onecake.ordersheet.service.OrderSheetService
import refresh.onecake.common.response.ApiResponse
import refresh.onecake.common.response.DefaultResponseDto

@RestController
@RequestMapping("/api/v1/")
class OrderSheetController (
    private val orderSheetService: OrderSheetService
){

    @Operation(summary = "소비자 - 주문하기 - GET 주문서 질문들 ")
    @GetMapping("consumer/stores/{storeId}/order/{menuId}")
    fun getOrderSheet(@PathVariable storeId: Long, @PathVariable menuId:Long): ResponseEntity<OrderSheetTwoTypeDto?> {
        return ApiResponse.success(
            HttpStatus.OK,
            orderSheetService.getOrderSheet(storeId, menuId)
        )
    }

    @Operation(summary = "소비자 - 주문하기 - POST 주문서 질문의 대답 ")
    @PostMapping("consumer/stores/{storeId}/order/{menuId}")
    fun postOrderSheet(
        @PathVariable storeId: Long,
        @PathVariable menuId: Long,
        @RequestBody answersDto: AnswersDto
    ): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            orderSheetService.postOrderSheet(storeId, menuId, answersDto)
        )
    }
}