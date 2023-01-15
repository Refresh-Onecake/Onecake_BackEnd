package refresh.onecake.menu.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import refresh.onecake.menu.entity.Image
import refresh.onecake.menu.entity.Keyword

@Repository
interface ImageRepository : JpaRepository<Image, Long>{
    fun findAllByMenuId(menuId: Long): List<Image>
    fun findImageById(id: Long): Image
    fun findAllByMenuIdAndIsActivatedAndKeywordIsNotNull(menuId: Long, isActivated: Boolean): List<Image>
    fun findTop10ByIsActivatedOrderByLikeNumDesc(isActivated: Boolean): List<Image>
    fun findFirstByKeywordAndIsActivatedOrderByCreatedAtDesc(keyword: Keyword, isActivated: Boolean): Image?


}