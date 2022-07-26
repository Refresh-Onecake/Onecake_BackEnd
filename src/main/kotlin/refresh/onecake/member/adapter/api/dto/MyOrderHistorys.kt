package refresh.onecake.member.adapter.api.dto

import refresh.onecake.member.domain.common.OrderState

data class MyOrderHistorys(
    val orderHistoryId: Long,
    val storeName: String,
    val orderState: OrderState,
    val menuName: String,
    val menuPrice: Int,
    val menuImage: String,
)
