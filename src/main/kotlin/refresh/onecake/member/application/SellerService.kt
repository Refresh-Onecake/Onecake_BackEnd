package refresh.onecake.member.application

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.member.adapter.api.dto.*
import refresh.onecake.member.application.util.SecurityUtil
import refresh.onecake.member.domain.common.*
import refresh.onecake.member.domain.member.MemberRepository
import refresh.onecake.member.domain.seller.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class SellerService (
    private val storeRepository: StoreRepository,
    private val addressRepository: AddressRepository,
    private val sellerRepository: SellerRepository,
    private val menuRepository: MenuRepository,
    private val questionRepository: QuestionRepository,
    private val dayOffRepository: DayOffRepository,
    private val imageRepository: ImageRepository,
    private val orderHistoryRepository: OrderHistoryRepository,
    private val orderSheetRepository: OrderSheetRepository,
    private val modelMapper: ModelMapper
){

    fun registerStore(applyStoreRequestDto: ApplyStoreRequestDto) : DefaultResponseDto{
        val id = SecurityUtil.getCurrentMemberId()
        if (storeRepository.existsById(id)) {
            return DefaultResponseDto(false, "이미 입점한 판매자 입니다.")
        }
        else {
            val address = Address(
                id = id,
                jibunAddress = applyStoreRequestDto.address?.jibunAddress,
                roadFullAddr = applyStoreRequestDto.address?.roadFullAddr,
                siNm = applyStoreRequestDto.address?.siNm,
                sggNm = applyStoreRequestDto.address?.sggNm,
                emdNm = applyStoreRequestDto.address?.emdNm,
                lnbrMnnm = applyStoreRequestDto.address?.lnbrMnnm,
                addressDetail = applyStoreRequestDto.address?.addressDetail
            )
            addressRepository.save(address)
            val store = Store(
                id = id,
                storeName = applyStoreRequestDto.storeName,
                businessRegistrationNumber = applyStoreRequestDto.businessRegistrationNumber,
                address = address,
                storePhoneNumber = applyStoreRequestDto.storePhoneNumber,
                storeDiscription = applyStoreRequestDto.storeDiscription,
                openTime = applyStoreRequestDto.openTime,
                closeTime = applyStoreRequestDto.closeTime,
                kakaoChannelUrl = applyStoreRequestDto.kakaoChannelUrl,
                storeImage = applyStoreRequestDto.storeImage
            )
            storeRepository.save(store)
            val seller = sellerRepository.getById(id)
            seller.store = store
            sellerRepository.save(seller)
            return DefaultResponseDto(true, "입점 신청을 완료하였습니다.")
        }
    }

    //TODO : menu에 image 추가
    fun registerMenu(applyMenuDto: ApplyMenuDto): DefaultResponseDto {

        val id = SecurityUtil.getCurrentMemberId()
//        if (menuRepository.existsByMenuSize(applyMenuDto.cakeSize)) {
//            return DefaultResponseDto(false, "이미 등록한 케이크 사이즈입니다.")
//        }
        val menu = Menu(
            store = storeRepository.getById(id),
            menuName = applyMenuDto.cakeSize + " 커스텀 케이크",
            menuSize = applyMenuDto.cakeSize,
            image = applyMenuDto.cakeImage,
            price = applyMenuDto.cakePrice,
            menuDescription = applyMenuDto.cakeDescription,
            taste = applyMenuDto.cakeTaste
        )
        val savedMenu = menuRepository.save(menu)

        val image = Image(
            menuId = savedMenu.id,
            image = applyMenuDto.cakeImage,
            keyword = null
        )
        imageRepository.save(image)

        if (applyMenuDto.consumerInput?.isNotEmpty() == true) {
            for (i in 0 until applyMenuDto.consumerInput!!.size){
                var question = Question(
                    menuId = savedMenu.id,
                    question = applyMenuDto.consumerInput!![i],
                    isConsumerInput = true
                )
                questionRepository.save(question)
            }
        }
        if (applyMenuDto.cakeInput?.isNotEmpty() == true) {
            for (i in 0 until applyMenuDto.cakeInput!!.size){
                var question = Question(
                    menuId = savedMenu.id,
                    question = applyMenuDto.cakeInput!![i],
                    isConsumerInput = false
                )
                questionRepository.save(question)
            }
        }

        return DefaultResponseDto(true, "메뉴 등록을 완료하였습니다.")
    }

    fun getMenus() : List<StoreMenuListDto>{
        val id = SecurityUtil.getCurrentMemberId()
        return menuRepository.findAllByStoreIdOrderByMenuNameAsc(id).map{ modelMapper.map(it, StoreMenuListDto::class.java) }
    }

    fun setDayOff(day: String): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val dayOff = dayOffRepository.findByStoreIdAndDay(id, day)
        return if (dayOff != null) {
            dayOffRepository.delete(dayOff)
            DefaultResponseDto(true, "휴무 일정을 취소하였습니다.")
        } else {
            dayOffRepository.save(DayOff(
                storeId = id,
                day= day
            ))
            DefaultResponseDto(true, "휴무 일정을 등록하였습니다.")
        }

    }

    fun getDayOff(day: String): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        return if (dayOffRepository.existsByStoreIdAndDay(id, day)) {
            DefaultResponseDto(true, "휴무로 지정된 날짜입니다.")
        } else {
            DefaultResponseDto(true, "휴무로 지정되지 않은 날짜입니다.")
        }
    }

    fun getSpecificDatesOrder(day: String): OrdersClassifiedByState {
        val id = SecurityUtil.getCurrentMemberId()
        var orders = orderHistoryRepository.findAllByStoreIdAndPickUpDay(id, day)

        var received = orders.filter { it.state == OrderState.RECEIVED }
        var accepted = orders.filter { it.state == OrderState.ACCEPTED }
        var making = orders.filter { it.state == OrderState.MAKING }
        var completed = orders.filter { it.state == OrderState.COMPLETED }
        var canceled = orders.filter { it.state == OrderState.CANCELED }

        return OrdersClassifiedByState(
            received = convertOrderHistoryToMenuDetail(received),
            accepted = convertOrderHistoryToMenuDetail(accepted),
            making = convertOrderHistoryToMenuDetail(making),
            completed = convertOrderHistoryToMenuDetail(completed),
            canceled = convertOrderHistoryToMenuDetail(canceled)
        )
    }

    fun convertOrderHistoryToMenuDetail(orderHistory: List<OrderHistory>): List<MenuThumbNail>?{
        var output: MutableList<MenuThumbNail>? = mutableListOf()
        for (i in orderHistory.indices) {
            var menu = menuRepository.findMenuById(orderHistory[i].menuId)
            println(menu.menuName)
            output?.add(MenuThumbNail(
                id = orderHistory[i].id,
                storeMenuListDto = StoreMenuListDto(
                    image = menu.image,
                    menuName = menu.menuName,
                    menuDescription = menu.menuDescription,
                    price = menu.price
                )
            ))
            println(output?.get(i)?.id)
        }
        return output?.toList()
    }

    fun getSpecificOrder(orderId: Long) : SpecificOrderForm{
        val order = orderHistoryRepository.findOrderHistoryById(orderId)
        val menu = menuRepository.findMenuById(order.menuId)
        val orderSheet = orderSheetRepository.findAllByOrderId(orderId)
        val answers = orderSheet?.map { it.answer }
        var questions: MutableList<String>? = mutableListOf()
        for (i in orderSheet?.indices!!) {
            questions?.add(i, questionRepository.findQuestionById(orderSheet[i].questionId).question + " : " + answers?.get(i))
        }
        return SpecificOrderForm(
            menuName = menu.menuName,
            price = menu.price,
            state = order.state,
            questions = questions,
            answers = answers,
            memo = order.memo
        )
    }

    fun postMemo(orderId: Long, memo:Memo): DefaultResponseDto {
        var order = orderHistoryRepository.findOrderHistoryById(orderId)
        order.memo = memo.memo
        orderHistoryRepository.save(order)
        return DefaultResponseDto(true, "메모를 저장하였습니다.")
    }

    fun changeOrderState(orderId: Long): DefaultResponseDto {
        var order = orderHistoryRepository.findOrderHistoryById(orderId)
        if (order.state == OrderState.RECEIVED) {
            order.state = OrderState.ACCEPTED
        } else if (order.state == OrderState.ACCEPTED) {
            order.state = OrderState.MAKING
        } else if (order.state == OrderState.MAKING) {
            order.state = OrderState.COMPLETED
        } else if (order.state == OrderState.COMPLETED) {
            order.state = OrderState.COMPLETED
        } else if (order.state == OrderState.CANCELED) {
            order.state = OrderState.RECEIVED
            order.reasonForCanceled = null
        }
        orderHistoryRepository.save(order)
        return DefaultResponseDto(true, "주문 상태를 다음 단계로 변경")
    }

    fun orderStateToCanceled(orderId: Long, cancelReason: CancelReason): DefaultResponseDto {
        val order = orderHistoryRepository.findOrderHistoryById(orderId)
        order.state = OrderState.CANCELED
        order.reasonForCanceled = cancelReason.reason
        orderHistoryRepository.save(order)
        return DefaultResponseDto(true, "주문 취소로 상태 변경")
    }

//    fun orderStateToMaking(orderId: Long): DefaultResponseDto {
//        val order = orderHistoryRepository.findOrderHistoryById(orderId)
//        order.state = OrderState.MAKING
//        orderHistoryRepository.save(order)
//        return DefaultResponseDto(true, "케이크 제작하기로 상태 변경")
//    }
//
//    fun orderStateToCompleted(orderId: Long): DefaultResponseDto {
//        val order = orderHistoryRepository.findOrderHistoryById(orderId)
//        order.state = OrderState.COMPLETED
//        orderHistoryRepository.save(order)
//        return DefaultResponseDto(true, "픽업 완료하기로 상태 변경")
//    }
//
//    fun orderStateToReceived(orderId: Long): DefaultResponseDto {
//        val order = orderHistoryRepository.findOrderHistoryById(orderId)
//        order.state = OrderState.RECEIVED
//        orderHistoryRepository.save(order)
//        return DefaultResponseDto(true, "다시 진행하기로 상태 변경")
//    }

}