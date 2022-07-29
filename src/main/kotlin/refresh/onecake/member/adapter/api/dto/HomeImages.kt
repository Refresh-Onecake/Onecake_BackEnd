package refresh.onecake.member.adapter.api.dto

import refresh.onecake.member.domain.seller.Keyword

data class HomeImages(
    val image: String,
    val storeId: Long,
    val menuId: Long,
    val imageId: Long
)

data class KeywordImages(
    val image: String,
    val storeId: Long,
    val menuId: Long,
    val imageId: Long,
    val keyword: Keyword
)

data class NeighborhoodStore (
    val storeImage: String = "",
    val id: Long = -1L
)
