package refresh.onecake.member.adapter.api.dto

import java.time.LocalDateTime

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
