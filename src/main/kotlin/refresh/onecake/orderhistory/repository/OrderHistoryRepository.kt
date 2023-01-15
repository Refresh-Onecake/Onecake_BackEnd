package refresh.onecake.orderhistory.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import refresh.onecake.orderhistory.entity.OrderHistory
import refresh.onecake.orderhistory.entity.OrderState

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
    fun  countByPickUpDayStartsWithAndStoreIdAndState(pickUpDay: String, storeId: Long, state: OrderState): Long

    @Query(nativeQuery = true, value = """
        select *
        from order_history o
        where 
            (o.pick_up_day like ?1%
            or o.pick_up_day like ?2%
            or o.pick_up_day like ?3%
            or o.pick_up_day like ?4%
            or o.pick_up_day like ?5%)
            and o.store_id = ?6
            and o.state = ?7
    """)
    fun getSalesGraphData(month: String, monthMinusOne: String, monthMinusTwo: String, monthMinusThree: String, monthMinusFour: String,
                          storeId: Long, state: String): List<OrderHistory>
}
