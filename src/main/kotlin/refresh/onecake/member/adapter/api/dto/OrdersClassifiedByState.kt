package refresh.onecake.member.adapter.api.dto

data class OrdersClassifiedByState(
    val received: List<MenuThumbNail>?,
    val accepted: List<MenuThumbNail>?,
    val making: List<MenuThumbNail>?,
    val completed: List<MenuThumbNail>?,
    val canceled: List<MenuThumbNail>?
)

data class MenuThumbNail(
    var id: Long,
    var storeMenuListDto: StoreMenuListDto
)

