package refresh.onecake.member.application

import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import refresh.onecake.member.adapter.api.dto.*
import refresh.onecake.member.adapter.infra.ForbiddenException
import refresh.onecake.member.application.util.SecurityUtil
import refresh.onecake.member.domain.common.*
import refresh.onecake.member.domain.member.MemberRepository
import refresh.onecake.member.domain.seller.*
import java.util.*

@Service
class SellerService (
    private val storeRepository: StoreRepository,
    private val addressRepository: AddressRepository,
    private val sellerRepository: SellerRepository,
    private val memberRepository: MemberRepository,
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
                storeImage = applyStoreRequestDto.storeImage,
                isActivated = true
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

        if (menuRepository.existsByMenuSizeAndStoreIdAndIsActivated(applyMenuDto.cakeSize, id, true)) {
            return DefaultResponseDto(false, "해당 메뉴 사이즈가 이미 존재합니다.")
        }

        val menu = Menu(
            storeId = id,
            menuName = applyMenuDto.cakeSize + " 커스텀 케이크",
            menuSize = applyMenuDto.cakeSize,
            image = applyMenuDto.cakeImage,
            price = applyMenuDto.cakePrice,
            menuDescription = applyMenuDto.cakeDescription,
            taste = applyMenuDto.cakeTaste,
            isActivated = true
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
                    isConsumerInput = true,
                    isActivated = true
                )
                questionRepository.save(question)
            }
        }
        if (applyMenuDto.cakeInput?.isNotEmpty() == true) {
            for (i in 0 until applyMenuDto.cakeInput!!.size){
                var question = Question(
                    menuId = savedMenu.id,
                    question = applyMenuDto.cakeInput!![i],
                    isConsumerInput = false,
                    isActivated = true
                )
                questionRepository.save(question)
            }
        }

        return DefaultResponseDto(true, "메뉴 등록을 완료하였습니다.")
    }

    fun getMenus() : List<StoreMenuListAndIdDto>{
        val id = SecurityUtil.getCurrentMemberId()
        return menuRepository.findAllByStoreIdAndIsActivatedOrderByMenuNameAsc(id, true).map{ modelMapper.map(it, StoreMenuListAndIdDto::class.java) }
    }

    fun deleteMenu(menuId: Long): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()

        val menu = menuRepository.findByIdOrNull(menuId) ?: throw ForbiddenException("존재하지 않는 menuId입니다.")

        if(menu.storeId != id) throw ForbiddenException("해당 메뉴의 판매자가 아닙니다.")

        menu.isActivated = false
        menuRepository.save(menu)
        return DefaultResponseDto(true, "메뉴 삭제를 성공했습니다.")
    }

    fun getStoredMenuForm(menuId: Long): StoredMenuForm {
        val menu = menuRepository.findMenuById(menuId)
        val question = questionRepository.findAllByMenuIdAndIsActivated(menuId, true)
        return StoredMenuForm(
            cakeSize = menu.menuSize,
            image = menu.image,
            price = menu.price,
            menuDescription = menu.menuDescription,
            taste = menu.taste,
            consumerInput = question.filter { it.isConsumerInput && it.isActivated }.map { it.question },
            cakeInput = question.filter { !it.isConsumerInput && it.isActivated }.map { it.question }

        )
    }

    fun editMenu(menuId: Long, applyMenuDto: ApplyMenuDto): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val menu = menuRepository.findByIdOrNull(menuId) ?: throw ForbiddenException("해당 메뉴 id는 존재하지 않습니다.")
        if(menu.storeId != id) throw ForbiddenException("해당 메뉴의 판매자가 아닙니다.")
        if (!menu.isActivated) throw ForbiddenException("해당 메뉴는 삭제된 상태입니다.")

        val isExistingMenuId = menuRepository.findByMenuSizeAndStoreIdAndIsActivated(applyMenuDto.cakeSize, id, true)?.id
        if (isExistingMenuId != null && menuId != isExistingMenuId) {
            return DefaultResponseDto(false, "해당 메뉴 사이즈가 이미 존재합니다.")
        }

        menu.id = menuId
        menu.storeId = id
        menu.menuName = applyMenuDto.cakeSize + " 커스텀 케이크"
        menu.menuSize = applyMenuDto.cakeSize
        menu.price = applyMenuDto.cakePrice
        menu.menuDescription = applyMenuDto.cakeDescription
        menu.taste = applyMenuDto.cakeTaste
        menu.image = applyMenuDto.cakeImage

        menuRepository.save(menu)

        val consumerInputQuestions = applyMenuDto.consumerInput
        val cakeInputQuestions = applyMenuDto.cakeInput
        val questions = questionRepository.findAllByMenuIdAndIsActivated(menuId, true)

//        consumerInputQuestion.forEach {
//            inputQuestion ->
//            if(questions.all{ it.question != inputQuestion })
//                questionRepository.save(Question(
//                    menuId = menuId,
//                    question = inputQuestion,
//                    isConsumerInput = true,
//                    isActivated = true,
//                ))
//        }

        questions.forEach { it.isActivated = false }
        questionRepository.saveAll(questions)

        for (i in consumerInputQuestions.indices) {
            questionRepository.save(
                Question(
                    menuId = menuId,
                    question = consumerInputQuestions[i],
                    isConsumerInput = true,
                    isActivated = true
                )
        ) }

        for (i in cakeInputQuestions.indices) {
            questionRepository.save(
                Question(
                    menuId = menuId,
                    question = cakeInputQuestions[i],
                    isConsumerInput = false,
                    isActivated = true
                )
        ) }

        return DefaultResponseDto(true, "메뉴 수정을 성공했습니다.")

    }

    fun setDayOff(dayAndName: DayAndName): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val dayOff = dayOffRepository.findByStoreIdAndDay(id, dayAndName.dayOff)
        return if (dayOff != null) {
            dayOffRepository.delete(dayOff)
            DefaultResponseDto(true, "휴무 일정을 취소하였습니다.")
        } else {
            dayOffRepository.save(DayOff(
                storeId = id,
                day= dayAndName.dayOff,
                dayOffName = dayAndName.dayOffName
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
        val id = SecurityUtil.getCurrentMemberId()
        var order = orderHistoryRepository.findOrderHistoryById(orderId)
        if (order.storeId != id) {
            throw ForbiddenException("요청을 보내는 유저는 해당 주문서의 판매자가 아닙니다.")
        }
        val menu = menuRepository.findMenuById(order.menuId)
        val orderSheet = orderSheetRepository.findAllByOrderId(orderId)
        val answers = orderSheet?.map { it.answer }
        val questionIds = orderSheet?.map { it.questionId }

        var forms: MutableList<String>? = mutableListOf()
        for (i in questionIds?.indices!!) {
            forms?.add(i, questionRepository.findQuestionById(questionIds[i]).question + " : " + answers?.get(i))
        }

        return SpecificOrderForm(
            menuName = menu.menuName,
            price = menu.price,
            state = order.state.toString().lowercase(),
            form = forms,
            memo = order.memo
        )
    }

    fun postMemo(orderId: Long, memo:Memo): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        var order = orderHistoryRepository.findOrderHistoryById(orderId)
        if (order.storeId != id) {
            throw ForbiddenException("메모를 등록하려는 유저는 해당 주문서의 판매자가 아닙니다.")
        }
        order.memo = memo.memo
        orderHistoryRepository.save(order)
        return DefaultResponseDto(true, "메모를 저장하였습니다.")
    }

    fun changeOrderState(orderId: Long): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        var order = orderHistoryRepository.findOrderHistoryById(orderId)
        if (order.storeId != id) {
            throw ForbiddenException("주문서 상태 변경을 시도하는 유저는 해당 주문서의 판매자가 아닙니다.")
        }

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
        val id = SecurityUtil.getCurrentMemberId()
        val order = orderHistoryRepository.findOrderHistoryById(orderId)
        if (order.storeId != id) {
            throw ForbiddenException("주문서 상태 변경을 시도하는 유저는 해당 주문서의 판매자가 아닙니다.")
        }

        order.state = OrderState.CANCELED
        order.reasonForCanceled = cancelReason.reason
        orderHistoryRepository.save(order)
        return DefaultResponseDto(true, "주문 취소로 상태 변경")
    }

    fun getSellerChatUrl(): String {
        val id = SecurityUtil.getCurrentMemberId()
        return storeRepository.getById(id).kakaoChannelUrl
    }

    fun resign(): DefaultResponseDto{
        val id = SecurityUtil.getCurrentMemberId()

        val member = memberRepository.getById(id)
        member.isActivated = false
        memberRepository.save(member)

        val store = storeRepository.findStoreById(id)
        store.isActivated = false
        storeRepository.save(store)

        return DefaultResponseDto(true, "탈퇴 처리가 완료되었습니다.")
    }

}