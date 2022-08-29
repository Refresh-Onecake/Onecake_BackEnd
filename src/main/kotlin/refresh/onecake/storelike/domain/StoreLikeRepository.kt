package refresh.onecake.storelike.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreLikeRepository: JpaRepository<StoreLike, Long> {
    fun existsByMemberIdAndStoreId(memberId: Long, storeId: Long): Boolean
    fun findStoreLikeByMemberIdAndStoreId(memberId: Long, storeId: Long): StoreLike?
    fun countByStoreId(storeId: Long): Long
}