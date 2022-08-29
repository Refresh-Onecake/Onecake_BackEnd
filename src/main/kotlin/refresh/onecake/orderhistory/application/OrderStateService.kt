package refresh.onecake.orderhistory.application

import org.springframework.stereotype.Service
import refresh.onecake.orderhistory.domain.OrderHistory
import refresh.onecake.orderhistory.domain.OrderState

@Service
class OrderStateService {

    fun changeOrderStateToKorean(orderHistory: OrderHistory) : String {
        return when (orderHistory.state) {
            OrderState.RECEIVED -> {
                "주문이 사장님께 전달되었어요!"
            }
            OrderState.ACCEPTED -> {
                "결제까지 완료되었어요!"
            }
            OrderState.MAKING -> {
                "케이크를 제작 중이에요!"
            }
            OrderState.COMPLETED -> {
                "케이크 제작이 완료되었어요!"
            }
            OrderState.PICKEDUP -> {
                "픽업이 완료되었어요!"
            }
            else -> {
                "주문이 취소되었습니다(" + orderHistory.reasonForCanceled + ")"
            }
        }
    }
}