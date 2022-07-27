package refresh.onecake.member.adapter.api

//import io.swagger.annotations.Api
//import io.swagger.annotations.Operation
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.member.adapter.api.dto.*
import refresh.onecake.member.application.SellerService

//@Api(tags = arrayOf(SwaggerNotes.SELLER_CONTROLLER_TAG), description = "입점 신청")
@RestController
@RequestMapping("/api/v1/seller")
class SellerController (
    private val sellerService: SellerService
){

    @Operation(summary = "입점 신청")
    @PostMapping("/store")
    fun registerStore(@RequestBody applyStoreRequestDto: ApplyStoreRequestDto): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.registerStore(applyStoreRequestDto))
    }




    /*
        메뉴
     */

    @Operation(summary = "메뉴 등록")
    @PostMapping("/store/menu")
    fun registerMenu(@RequestBody applyMenuDto: ApplyMenuDto): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.registerMenu(applyMenuDto))
    }

    @Operation(summary = "메뉴 보기")
    @GetMapping("/store/menu")
    fun getMenus(): ResponseEntity<List<StoreMenuListAndIdDto>> {
        return ApiResponse.success(HttpStatus.OK, sellerService.getMenus())
    }

    @Operation(summary = "메뉴 삭제")
    @DeleteMapping("/store/menu/{menuId}")
    fun deleteMenu(@PathVariable menuId: Long): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.deleteMenu(menuId))
    }

    @Operation(summary = "특정 메뉴의 이미지들 불러오기")
    @GetMapping("/store/menu/{menuId}/image")
    fun getImagesOfSpecificMenu(@PathVariable menuId: Long): ResponseEntity<MenuImageSetting> {
        return ApiResponse.success(HttpStatus.OK, sellerService.getImagesOfSpecificMenu(menuId))
    }

    @Operation(summary = "특정 메뉴의 이미지 추가하기")
    @PostMapping("/store/menu/{menuId}/image")
    fun postImage(
        @PathVariable menuId: Long,
        @RequestBody imageAndKeyword: ImageAndKeyword
    ): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.postImage(menuId, imageAndKeyword))
    }

    @Operation(summary = "특정 이미지 불러오기")
    @GetMapping("/store/menu/{menuId}/image/{imageId}")
    fun getImageDetail(@PathVariable menuId: Long, @PathVariable imageId: Long): ResponseEntity<ImageDetail>{
        return ApiResponse.success(HttpStatus.OK, sellerService.getImageDetail(menuId, imageId))
    }

    @Operation(summary = "특정 이미지 좋아요하기")
    @PostMapping("/store/menu/{menuId}/image/{imageId}/like")
    fun postImageLike(@PathVariable menuId: Long, @PathVariable imageId: Long): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.postImageLike(menuId, imageId))
    }

    @Operation(summary = "특정 이미지 삭제하기")
    @DeleteMapping("/store/menu/{menuId}/image/{imageId}")
    fun deleteImage(@PathVariable menuId: Long, @PathVariable imageId: Long): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.deleteImage(imageId))
    }

    /*
        메뉴 수정
     */

    @Operation(summary = "메뉴 수정")
    @PutMapping("/store/menu/{menuId}")
    fun editMenu(@PathVariable menuId: Long, @RequestBody applyMenuDto: ApplyMenuDto): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.editMenu(menuId, applyMenuDto))
    }

    @Operation(summary = "저장되어있던 메뉴및 주문서 데이터 불러오기")
    @GetMapping("/store/menu/{menuId}")
    fun getStoredMenuForm(@PathVariable menuId: Long): ResponseEntity<StoredMenuForm> {
        return ApiResponse.success(HttpStatus.OK, sellerService.getStoredMenuForm(menuId))
    }




    /*
        휴일
     */

    @Operation(summary = "휴일 지정")
    @PostMapping("/store/dayOff")
    fun setDayOff(@RequestBody dayAndName: DayAndName): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.setDayOff(dayAndName))
    }

    @Operation(summary = "휴일 가져오기")
    @GetMapping("/store/dayOff/{dayOff}")
    fun getDayOff(@PathVariable dayOff: String): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.getDayOff(dayOff))
    }



    /*
        주문서
     */

    @Operation(summary = "특정 날짜의 주문들 가져오기")
    @GetMapping("/store/order/{day}")
    fun getSpecificDatesOrder(@PathVariable day: String): ResponseEntity<OrdersClassifiedByState> {
        return ApiResponse.success(HttpStatus.OK, sellerService.getSpecificDatesOrder(day))
    }

    @Operation(summary = "특정 주문서 가져오기")
    @GetMapping("/store/order/form/{orderId}")
    fun getSpecificOrder(@PathVariable orderId: Long): ResponseEntity<SpecificOrderForm> {
        return ApiResponse.success(HttpStatus.OK, sellerService.getSpecificOrder(orderId))
    }

    @Operation(summary = "특정 주문서에 메모 저장하기")
    @PostMapping("/store/order/form/{orderId}/memo")
    fun postMemo(@PathVariable orderId: Long, @RequestBody memo: Memo): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.postMemo(orderId, memo))
    }

    @Operation(summary = "주문 상태 다음 단계로 변경")
    @PostMapping("/store/order/form/{orderId}/state")
    fun orderStateToAccepted(@PathVariable orderId: Long): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.changeOrderState(orderId))
    }

    @Operation(summary = "주문 취소로 상태 변경")
    @PostMapping("/store/order/form/{orderId}/state/cancel")
    fun orderStateToCanceled(@PathVariable orderId: Long, @RequestBody cancelReason: CancelReason): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.orderStateToCanceled(orderId, cancelReason))
    }




    /*
        기타
     */

    @Operation(summary = "판매자의 카카오톡 url 가져오기")
    @GetMapping("/chat")
    fun getSellerChatUrl(): ResponseEntity<String> {
        return ApiResponse.success(HttpStatus.OK, sellerService.getSellerChatUrl())
    }

    @Operation(summary = "판매자 회원탈퇴하기")
    @PostMapping("/resign")
    fun resign(): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, sellerService.resign())
    }

}