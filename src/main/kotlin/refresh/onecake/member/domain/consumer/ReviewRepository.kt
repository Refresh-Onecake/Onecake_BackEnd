package refresh.onecake.member.domain.consumer

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository : JpaRepository<Review, Long>{
    fun countByStoreId(storeId: Long): Long
}