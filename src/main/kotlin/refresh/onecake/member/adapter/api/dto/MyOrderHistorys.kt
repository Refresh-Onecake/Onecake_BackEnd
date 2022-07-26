package refresh.onecake.member.adapter.api.dto

import refresh.onecake.member.domain.common.OrderState

data class MyOrderHistorys(
    val orderHistoryId: Long,
    val storeName: String,
    val orderState: OrderState,
    val menuName: String,
    val menuPrice: Int,
    val menuImage: String,
    val hasReview: Boolean
)

data class SpecificOrderHistory(
    val storeName: String,
    val orderState: String,
    val orderTime: String,
    val pickUpTime: String,
    val menuName: String,
    val menuPrice: Int,
    val form: List<String>
)
