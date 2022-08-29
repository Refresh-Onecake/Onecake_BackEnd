package refresh.onecake.menu.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<Image, Long>{
    fun findAllByMenuId(menuId: Long): List<Image>
    fun findImageById(id: Long): Image
    fun findAllByMenuIdAndIsActivatedAndKeywordIsNotNull(menuId: Long, isActivated: Boolean): List<Image>
    fun findTop10ByIsActivatedOrderByLikeNumDesc(isActivated: Boolean): List<Image>
    fun findFirstByKeywordAndIsActivatedOrderByCreatedAtDesc(keyword: Keyword, isActivated: Boolean): Image?


}