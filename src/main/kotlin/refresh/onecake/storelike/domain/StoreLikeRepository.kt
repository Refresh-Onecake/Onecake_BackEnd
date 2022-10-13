package refresh.onecake.storelike.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import refresh.onecake.storelike.adapter.dto.LikedStore

@Repository
interface StoreLikeRepository: JpaRepository<StoreLike, Long> {
    fun existsByMemberIdAndStoreId(memberId: Long, storeId: Long): Boolean
    fun findStoreLikeByMemberIdAndStoreId(memberId: Long, storeId: Long): StoreLike?
    fun countByStoreId(storeId: Long): Long
    fun findAllByMemberId(memberId: Long): List<StoreLike>

    @Query(value = """
        select new refresh.onecake.storelike.adapter.dto.LikedStore(s.id, s.storeImage, s.storeName)
        from Store s, StoreLike l
        where l.memberId = ?1
        and s.id = l.storeId 
    """)
    fun getStoresLikedByConsumer(memberId: Long): List<LikedStore>
}