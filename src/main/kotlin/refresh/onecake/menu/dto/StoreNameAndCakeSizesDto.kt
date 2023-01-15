package refresh.onecake.menu.dto

data class StoreNameAndCakeSizesDto(
    var storeName: String,
    var sizes: List<MenuIdAndSizeDto>?
)

data class MenuIdAndSizeDto (
    var id: Long,
    var menuSize: String
)