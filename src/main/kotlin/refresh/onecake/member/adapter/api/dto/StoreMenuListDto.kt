package refresh.onecake.member.adapter.api.dto

data class StoreMenuListDto(
    var image: String = "",
    var menuName: String = "",
    var menuDescription: String = "",
    var price: Int = 0
)

data class StoreMenuListAndIdDto(
    var id: Long = -1L,
    var image: String = "",
    var menuName: String = "",
    var menuDescription: String = "",
    var price: Int = 0
)
