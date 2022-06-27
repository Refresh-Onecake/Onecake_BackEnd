package refresh.onecake.member.application

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import refresh.onecake.member.adapter.api.dto.*
import refresh.onecake.member.application.util.SecurityUtil
import refresh.onecake.member.domain.common.*
import refresh.onecake.member.domain.member.MemberRepository
import refresh.onecake.member.domain.seller.AddressRepository
import refresh.onecake.member.domain.seller.MenuRepository
import refresh.onecake.member.domain.seller.StoreRepository

@Service
class ConsumerService (
    private val memberRepository: MemberRepository,
    private val storeRepository: StoreRepository,
    private val menuRepository: MenuRepository,
    private val addressRepository: AddressRepository,
    private val questionRepository: QuestionRepository,
    private val orderHistoryRepository: OrderHistoryRepository,
    private val orderSheetRepository: OrderSheetRepository,
    private val modelMapper: ModelMapper
){

    fun storeMainInfo(storeId:Long): StoreMainInfoDto {
        val store = storeRepository.getById(storeId)
        return StoreMainInfoDto(storeImage = store.storeImage,
                                storeName = store.storeName,
                                storeDescription = store.storeDiscription,
                                likedNum = 0,
                                reviewNum = 0)
    }

    fun storeMenuList(storeId: Long): List<StoreMenuListDto>? {
        return menuRepository.findAllByStoreIdOrderByMenuSizeAsc(storeId)
            ?.map { modelMapper.map(it, StoreMenuListDto::class.java) }
    }

    fun getStoreInformation(storeId: Long): StoreDetailInfoDto {
        val store = storeRepository.getById(storeId)
        return StoreDetailInfoDto(
            operatingTime = store.openTime + " ~ " + store.closeTime,
            dayOff = "주문 시 확인",
            address = addressRepository.getById(storeId).roadFullAddr.orEmpty(),
            storeDescription = store.storeDiscription
        )
    }

    fun getCakesSize(storeId: Long): List<MenuIdAndSizeDto>?{
        return menuRepository.findAllIdAndMenuSizeByStoreIdOrderByMenuSizeAsc(storeId)
            ?.map{modelMapper.map(it, MenuIdAndSizeDto::class.java)}
    }

    fun getOrderSheet(storeId: Long, menuId: Long): OrderSheetTwoTypeDto? {
        val questions = questionRepository.findAllByMenuId(menuId)
        return OrderSheetTwoTypeDto(
//            consumerInput = questions?.filter { it.isConsumerInput }?.map { modelMapper.map(it, IdAndQuestionDto::class.java) },
//            cakeInput = questions?.filter { !it.isConsumerInput }?.map { modelMapper.map(it, IdAndQuestionDto::class.java) }
            consumerInput = questions?.filter { it.isConsumerInput }?.map { question -> question.question },
            cakeInput = questions?.filter { !it.isConsumerInput }?.map { question -> question.question }
        )
    }

    fun postOrderSheet(storeId: Long, menuId: Long, answersDto: AnswersDto?): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val orderHistory = OrderHistory(
            userId = id,
            storeId = storeId,
            menuId = menuId,
            state = OrderState.RECEIVED
        )
        val savedOrderHistory = orderHistoryRepository.save(orderHistory)

        val questions = questionRepository.findAllByMenuId(menuId)

        if (questions != null) {
            for (i in questions.indices) {
                var orderSheet = OrderSheet(
                    questionId = questions[i].id,
                    orderId = savedOrderHistory.id,
                    answer = answersDto?.answers!![i]
                )
                orderSheetRepository.save(orderSheet)
            }
        }

        val member = memberRepository.getById(id).userName

        return DefaultResponseDto(true, member + "님의 주문이 성공적으로 접수되었습니다!")

    }
}