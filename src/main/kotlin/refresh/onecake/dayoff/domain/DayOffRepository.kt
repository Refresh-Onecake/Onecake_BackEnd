package refresh.onecake.dayoff.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface DayOffRepository : JpaRepository<DayOff, Long> {

    fun findByStoreIdAndDay(storeId: Long, day: String): DayOff?
    fun existsByStoreIdAndDay(storeId: Long, day: String): Boolean
    fun findAllByStoreIdOrderByDayAsc(storeId: Long): List<DayOff>

    fun deleteAllInBatchByStoreId(storeId: Long)

    @Transactional
    @Query(nativeQuery = true, value = """
        delete 
        from day_off d 
        where d.day like '?1%' 
                and d.store_id = ?2 
    """)
    fun deleteAllByStoreIdStartsWithSpecificMonth(@Param("day") day: String, @Param("store_id") storeId: Long)

    @Transactional
    fun deleteAllByDayStartsWithAndStoreId(day: String, storeId: Long)
}