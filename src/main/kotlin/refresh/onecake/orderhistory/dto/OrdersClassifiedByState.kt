package refresh.onecake.orderhistory.dto

import refresh.onecake.menu.dto.StoreMenuListDto

data class OrdersClassifiedByState(
    val received: List<MenuThumbNail>?,
    val accepted: List<MenuThumbNail>?,
    val making: List<MenuThumbNail>?,
    val completed: List<MenuThumbNail>?,
    val pickedUp: List<MenuThumbNail>?,
    val canceled: List<MenuThumbNail>?
)

data class MenuThumbNail(
    var id: Long,
    var storeMenuListDto: StoreMenuListDto
)

