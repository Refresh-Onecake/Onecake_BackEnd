package refresh.onecake.storelike.dto

data class LikedStore(
    val id: Long,
    val image: String,
    val name: String
)
data class LikedStoreWithReviewNum(
    val id: Long,
    val image: String,
    val name: String,
    val reviewNum: Long
)
