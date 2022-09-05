package refresh.onecake.orderhistory.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface OrderHistoryRepository : JpaRepository<OrderHistory, Long>{
    fun findAllByStoreIdAndPickUpDay(storeId: Long, pickUpDay: String): List<OrderHistory>
    fun findOrderHistoryById(id: Long): OrderHistory
    fun findAllByUserId(userId: Long): List<OrderHistory>

//    @Query(nativeQuery = true, value = """
//        select o.state as state
//        from order_history o
//        where o.pick_up_date like '?1%'
//                and o.store_id = ?2
//    """)
//    fun getStatesForStatistics(pickUpDate: LocalDate, storeId: Long): List<LocalDate>

    fun countByPickUpDayStartsWithAndStoreId(pickUpDay: String, storeId: Long): Long
    fun countByPickUpDayStartsWithAndStoreIdAndState(pickUpDay: String, storeId: Long, state:OrderState): Long
}