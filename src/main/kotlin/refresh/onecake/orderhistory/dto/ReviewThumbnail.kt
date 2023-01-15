package refresh.onecake.orderhistory.dto

data class ReviewThumbnail(
    val profileImg: String?,
    val userName: String,
    val timeHistory: String,
    val content: String
)

data class ReviewAndNum(
    val reviewNum: Int,
    val reviews: List<ReviewThumbnail>
)
