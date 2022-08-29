package refresh.onecake.imagelike.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageLikeRepository: JpaRepository<ImageLike, Long> {
    fun existsByMemberIdAndImageId(memberId: Long, imageId: Long): Boolean
    fun findImageLikeByMemberIdAndImageId(memberId: Long, imageId: Long): ImageLike?
//    fun countByStoreId(storeId: Long): Long
}