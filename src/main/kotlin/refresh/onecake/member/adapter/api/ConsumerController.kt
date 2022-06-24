package refresh.onecake.member.adapter.api

import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import refresh.onecake.member.adapter.api.dto.StoreMainInfoDto
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

}