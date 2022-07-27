package refresh.onecake.member.domain.seller

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<Image, Long>{
    fun findAllByMenuId(menuId: Long): List<Image>
    fun findImageById(id: Long): Image
    fun findAllByMenuIdAndKeywordIsNotNull(menuId: Long): List<Image>
}