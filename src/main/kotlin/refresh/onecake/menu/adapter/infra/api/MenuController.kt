package refresh.onecake.menu.adapter.infra.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import refresh.onecake.menu.adapter.infra.dto.*
import refresh.onecake.menu.application.MenuService
import refresh.onecake.response.adapter.api.ApiResponse
import refresh.onecake.response.adapter.dto.DefaultResponseDto

@RestController
@RequestMapping("/api/v1/")
class MenuController (
    private val menuService: MenuService,
){

    @Operation(summary = "소비자 - 주문하기 - 케잌사이즈들 ")
    @GetMapping("consumer/stores/{storeId}/order/cakesize")
    fun getCakesSize(@PathVariable storeId: Long): ResponseEntity<StoreNameAndCakeSizesDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            menuService.getCakesSize(storeId)
        )
    }

    @Operation(summary = "소비자 - 이번주 핫한 케이크 이미지 불러오기")
    @GetMapping("consumer/home/hot")
    fun getWeeksHottestCake(): ResponseEntity<List<HomeImages>>{
        return ApiResponse.success(
            HttpStatus.OK,
            menuService.getWeeksHottestCake()
        )
    }




    @Operation(summary = "메뉴 등록")
    @PostMapping("/seller/store/menu")
    fun registerMenu(@RequestBody applyMenuDto: ApplyMenuDto): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            menuService.registerMenu(applyMenuDto)
        )
    }

    @Operation(summary = "메뉴 보기")
    @GetMapping("/seller/store/menu")
    fun getMenus(): ResponseEntity<List<StoreMenuListAndIdDto>> {
        return ApiResponse.success(
            HttpStatus.OK,
            menuService.getMenus()
        )
    }

    @Operation(summary = "메뉴 삭제")
    @DeleteMapping("/seller/store/menu/{menuId}")
    fun deleteMenu(@PathVariable menuId: Long): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            menuService.deleteMenu(menuId)
        )
    }

    @Operation(summary = "메뉴 수정")
    @PutMapping("/seller/store/menu/{menuId}")
    fun editMenu(@PathVariable menuId: Long, @RequestBody applyMenuDto: ApplyMenuDto): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            menuService.editMenu(menuId, applyMenuDto)
        )
    }

    @Operation(summary = "저장되어있던 메뉴및 주문서 데이터 불러오기")
    @GetMapping("/seller/store/menu/{menuId}")
    fun getStoredMenuForm(@PathVariable menuId: Long): ResponseEntity<StoredMenuForm> {
        return ApiResponse.success(
            HttpStatus.OK,
            menuService.getStoredMenuForm(menuId)
        )
    }
}