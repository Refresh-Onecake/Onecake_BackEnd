package refresh.onecake.dayoff.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DayOffRepository : JpaRepository<DayOff, Long> {

    fun findByStoreIdAndDay(storeId: Long, day: String): DayOff?
    fun existsByStoreIdAndDay(storeId: Long, day: String): Boolean
    fun findAllByStoreIdOrderByDayAsc(storeId: Long): List<DayOff>
}