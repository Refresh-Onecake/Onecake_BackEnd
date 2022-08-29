package refresh.onecake.review.adapter.infra.dto

data class PostReview(
    val orderId: Long,
    val content: String,
    val image: String,
    val price: String
)
