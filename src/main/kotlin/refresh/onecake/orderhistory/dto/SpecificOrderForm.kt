package refresh.onecake.orderhistory.dto

data class SpecificOrderForm(
    var menuName: String,
    var price: Int,
    var state: String,
    var form: List<String>?,
    var memo: String?
)
