package refresh.onecake.orderhistory.adapter.infra.dto

data class SpecificOrderForm(
    var menuName: String,
    var price: Int,
    var state: String,
    var form: List<String>?,
    var memo: String?
)
