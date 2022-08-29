package refresh.onecake.review.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository : JpaRepository<Review, Long>{
    fun countByStoreId(storeId: Long): Long
    fun findAllByStoreId(storeId: Long): List<Review>?
    fun existsByStoreIdAndConsumerIdAndMenuId(storeId: Long, consumerId: Long, menuId:Long): Boolean
    fun existsByOrderHistoryId(orderHistoryId:Long): Boolean
}