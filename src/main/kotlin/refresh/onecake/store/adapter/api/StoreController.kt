package refresh.onecake.store.adapter.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import refresh.onecake.menu.adapter.infra.dto.NeighborhoodStore
import refresh.onecake.menu.adapter.infra.dto.StoreMenuListDto
import refresh.onecake.response.adapter.api.ApiResponse
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.store.adapter.dto.*
import refresh.onecake.store.application.StoreService

@RestController
@RequestMapping("/api/v1/")
class StoreController (
    private val storeService: StoreService
){
    @Operation(summary = "소비자 - 상세 가게 페이지 - 윗부분 ")
    @GetMapping("consumer/stores/{storeId}/mainInfo")
    fun storeMainInfo(@PathVariable storeId:Long): ResponseEntity<StoreMainInfoDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            storeService.storeMainInfo(storeId)
        )
    }

    @Operation(summary = "소비자 - 상세 가게 페이지 - 아랫부분 - 메뉴 ")
    @GetMapping("consumer/stores/{storeId}/menuList")
    fun storeMenuList(@PathVariable storeId:Long): ResponseEntity<List<StoreMenuListDto>?> {
        return ApiResponse.success(
            HttpStatus.OK,
            storeService.storeMenuList(storeId)
        )
    }

    @Operation(summary = "소비자 - 상세 가게 페이지 - 아랫부분 - 가게정보 ")
    @GetMapping("consumer/stores/{storeId}/storeInfo")
    fun getStoreInformation(@PathVariable storeId: Long): ResponseEntity<StoreDetailInfoDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            storeService.getStoreInformation(storeId)
        )
    }

    @Operation(summary = "소비자 - 가게 탭 (해당 지역의 모든 가게 보기) & 인기순 ")
    @GetMapping("consumer/stores/main")
    fun getAllStoreByAddress(@RequestBody addressAndFilter: AddressAndFilter): ResponseEntity<List<StoreThumbNail>?> {
        return ApiResponse.success(
            HttpStatus.OK,
            storeService.getAllStoreByAddressAndFilter(addressAndFilter)
        )
    }

    @Operation(summary = "소비자 - 우리 동네 케이크 불러오기")
    @GetMapping("consumer/home/neighborhood")
    fun getNeighborhoodStore(): ResponseEntity<List<NeighborhoodStore>> {
        return ApiResponse.success(
            HttpStatus.OK,
            storeService.getNeighborhoodStore()
        )
    }





    @Operation(summary = "입점 신청")
    @PostMapping("/seller/store")
    fun registerStore(@RequestBody applyStoreRequestDto: ApplyStoreRequestDto): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            storeService.registerStore(applyStoreRequestDto)
        )
    }

    @Operation(summary = "판매자의 카카오톡 url 가져오기")
    @GetMapping("/seller/chat")
    fun getSellerChatUrl(): ResponseEntity<String> {
        return ApiResponse.success(
            HttpStatus.OK,
            storeService.getSellerChatUrl()
        )
    }

    @Operation(summary = "판매 통계")
    @GetMapping("/seller/store/statistics/{month}")
    fun getSalesData(@PathVariable month:String): ResponseEntity<SalesData> {
        return ApiResponse.success(
            HttpStatus.OK,
            storeService.getSalesData(month)
        )
    }

    @Operation(summary = "판매 그래프 데이터")
    @GetMapping("/seller/store/chart/statistics/{month}")
    fun getSalesGraphData(@PathVariable month:String): ResponseEntity<GraphData> {
        return ApiResponse.success(
            HttpStatus.OK,
            storeService.getSalesGraphData(month)
        )
    }

}