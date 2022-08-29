package refresh.onecake.store.adapter.dto

data class StoreMainInfoDto (
    var storeName: String,
    var storeDescription: String,
    var storeImage: String,
    var likeNum: Long,
    var isLiked: Boolean
)