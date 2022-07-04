package refresh.onecake.member.adapter.api.dto

data class StoreNameAndCakeSizesDto(
    var storeName: String,
    var sizes: List<MenuIdAndSizeDto>?
)
