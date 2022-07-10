package refresh.onecake.member.adapter.api.dto

data class PostReview(
    val orderId: Long,
    val content: String,
    val image: String,
    val price: String
)
