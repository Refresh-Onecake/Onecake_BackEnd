package refresh.onecake.ordersheet.dto

data class OrderSheetTwoTypeDto(
    var menuName: String,
    var consumerInput: List<String>?,
    var cakeInput: List<String>?,
    var menuPrice: Int
)
