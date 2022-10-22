package refresh.onecake.orderhistory.application

import org.springframework.stereotype.Service
import refresh.onecake.fcm.FcmService
import refresh.onecake.member.application.SecurityUtil
import refresh.onecake.menu.adapter.infra.dto.StoreMenuListDto
import refresh.onecake.menu.domain.MenuRepository
import refresh.onecake.orderhistory.adapter.infra.dto.*
import refresh.onecake.orderhistory.domain.OrderHistory
import refresh.onecake.orderhistory.domain.OrderHistoryRepository
import refresh.onecake.orderhistory.domain.OrderState
import refresh.onecake.ordersheet.application.QuestionsAndAnswers
import refresh.onecake.response.adapter.api.ForbiddenException
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.review.domain.ReviewRepository
import refresh.onecake.store.domain.StoreRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class OrderHistoryService (
    private val orderHistoryRepository: OrderHistoryRepository,
    private val storeRepository: StoreRepository,
    private val menuRepository: MenuRepository,
    private val questionsAndAnswers: QuestionsAndAnswers,
    private val reviewRepository: ReviewRepository,
    private val orderStateService: OrderStateService,
    private val fcmService: FcmService
){

    fun getOrderHistorys(): List<MyOrderHistorys> {
        val id = SecurityUtil.getCurrentMemberId()
        val orderHistorys = orderHistoryRepository.findAllByUserId(id)
        val outputs: MutableList<MyOrderHistorys> = mutableListOf()
        for (i in orderHistorys.indices) {
            val orderHistory = orderHistorys[i]
            val store = storeRepository.findStoreById(orderHistory.storeId)
            val menu = menuRepository.findMenuById(orderHistory.menuId)
            outputs.add(
                MyOrderHistorys(
                    orderHistoryId = orderHistory.id,
                    storeName = store.storeName,
                    orderState = orderHistory.state,
                    menuName = menu.menuName,
                    menuPrice = menu.price,
                    menuImage = menu.image,
                    hasReview = reviewRepository.existsByOrderHistoryId(orderHistory.id)
                )
            )
        }
        return outputs
    }

    fun getSpecificOrderHistory(orderHistoryId: Long): SpecificOrderHistory {
        val orderHistory = orderHistoryRepository.findOrderHistoryById(orderHistoryId)
        val store = storeRepository.findStoreById(orderHistory.storeId)
        val menu = menuRepository.findMenuById(orderHistory.menuId)

        val orderTime = orderHistory.createdAt.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a hh:mm").withLocale(Locale.KOREA))
        val pickUpDate = orderHistory.pickUpDay.format(DateTimeFormatter.ISO_DATE).format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 "))
        val pickUpTime = LocalTime.parse(orderHistory.pickUpTime).format(DateTimeFormatter.ofPattern("a hh:mm").withLocale(Locale.KOREA))

        return SpecificOrderHistory(
            storeName = store.storeName,
            orderState = orderStateService.changeOrderStateToKorean(orderHistory),
            orderTime = orderTime,
            pickUpTime = pickUpDate + pickUpTime,
            menuName = menu.menuName,
            menuPrice = menu.price,
            form = questionsAndAnswers.getQuestionsAndAnswersWhoseConsumerInputIsFalse(orderHistoryId)
        )
    }


    fun getSpecificDatesOrder(day: String): OrdersClassifiedByState {
        val id = SecurityUtil.getCurrentMemberId()
        val orders = orderHistoryRepository.findAllByStoreIdAndPickUpDay(id, day)

        val received = orders.filter { it.state == OrderState.RECEIVED }
        val accepted = orders.filter { it.state == OrderState.ACCEPTED }
        val making = orders.filter { it.state == OrderState.MAKING }
        val completed = orders.filter { it.state == OrderState.COMPLETED }
        val pickedup = orders.filter { it.state == OrderState.PICKEDUP }
        val canceled = orders.filter { it.state == OrderState.CANCELED }

        return OrdersClassifiedByState(
            received = convertOrderHistoryToMenuDetail(received),
            accepted = convertOrderHistoryToMenuDetail(accepted),
            making = convertOrderHistoryToMenuDetail(making),
            completed = convertOrderHistoryToMenuDetail(completed),
            pickedUp = convertOrderHistoryToMenuDetail(pickedup),
            canceled = convertOrderHistoryToMenuDetail(canceled)
        )
    }

    fun convertOrderHistoryToMenuDetail(orderHistory: List<OrderHistory>): List<MenuThumbNail>?{
        val output: MutableList<MenuThumbNail> = mutableListOf()
        for (i in orderHistory.indices) {
            val menu = menuRepository.findMenuById(orderHistory[i].menuId)
            println(menu.menuName)
            output.add(
                MenuThumbNail(
                    id = orderHistory[i].id,
                    storeMenuListDto = StoreMenuListDto(
                        image = menu.image,
                        menuName = menu.menuName,
                        menuDescription = menu.menuDescription,
                        price = menu.price
                    )
                )
            )
            println(output[i].id)
        }
        return output.toList()
    }


    fun getSpecificOrder(orderId: Long) : SpecificOrderForm {
        val id = SecurityUtil.getCurrentMemberId()
        val order = orderHistoryRepository.findOrderHistoryById(orderId)
        if (order.storeId != id) {
            throw ForbiddenException("요청을 보내는 유저는 해당 주문서의 판매자가 아닙니다.")
        }
        val menu = menuRepository.findMenuById(order.menuId)

        return SpecificOrderForm(
            menuName = menu.menuName,
            price = menu.price,
            state = order.state.toString().lowercase(),
            form = questionsAndAnswers.getQuestionsAndAnswers(orderId),
            memo = order.memo
        )
    }

    fun postMemo(orderId: Long, memo: Memo): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val order = orderHistoryRepository.findOrderHistoryById(orderId)
        if (order.storeId != id) {
            throw ForbiddenException("메모를 등록하려는 유저는 해당 주문서의 판매자가 아닙니다.")
        }
        order.memo = memo.memo
        orderHistoryRepository.save(order)
        return DefaultResponseDto(true, "메모를 저장하였습니다.")
    }

    fun changeOrderState(orderId: Long): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val order = orderHistoryRepository.findOrderHistoryById(orderId)
        if (order.storeId != id) {
            throw ForbiddenException("주문서 상태 변경을 시도하는 유저는 해당 주문서의 판매자가 아닙니다.")
        }

        when (order.state) {
            OrderState.RECEIVED -> {
                order.state = OrderState.ACCEPTED
            }
            OrderState.ACCEPTED -> {
                order.state = OrderState.MAKING
            }
            OrderState.MAKING -> {
                order.state = OrderState.COMPLETED
            }
            OrderState.COMPLETED -> {
                order.state = OrderState.PICKEDUP

            }
            OrderState.PICKEDUP -> {
                order.state = OrderState.PICKEDUP
            }
            OrderState.CANCELED -> {
                order.state = OrderState.RECEIVED
                order.reasonForCanceled = null
            }
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
}