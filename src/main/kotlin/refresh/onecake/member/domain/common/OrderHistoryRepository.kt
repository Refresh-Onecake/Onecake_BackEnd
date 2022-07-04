package refresh.onecake.member.domain.common

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderHistoryRepository : JpaRepository<OrderHistory, Long>{
    fun findAllByStoreIdAndPickUpDay(storeId: Long, pickUpDay: String): List<OrderHistory>
    fun findOrderHistoryById(id: Long): OrderHistory
}