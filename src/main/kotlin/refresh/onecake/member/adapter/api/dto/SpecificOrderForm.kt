package refresh.onecake.member.adapter.api.dto

import refresh.onecake.member.domain.common.OrderState

data class SpecificOrderForm(
    var menuName: String,
    var price: Int,
    var state: String,
    var form: List<String>?,
    var memo: String?
)
