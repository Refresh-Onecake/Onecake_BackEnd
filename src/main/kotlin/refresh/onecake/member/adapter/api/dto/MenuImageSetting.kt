package refresh.onecake.member.adapter.api.dto

import refresh.onecake.member.domain.seller.Keyword

data class MenuImageSetting(
    val storeName: String,
    val menuTaste: String,
    val images: List<ImageIdAndImage>
)

data class ImageIdAndImage(
    val id: Long = -1L,
    val image: String = ""
)

data class ImageAndKeyword(
    val image: String,
    val keyword: Keyword
)

data class ImageDetail(
    val storeName: String,
    val keyWord: String,
    val imageDescription: String,
    val image: String,
    val isLiked: Boolean
)

interface MenuIdAndStoreId {
    val menuId: Long
    val storeId: Long
}

interface QuestionAndAnswer{
    val question: String
    val answer: String
}