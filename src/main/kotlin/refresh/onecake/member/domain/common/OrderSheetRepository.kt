package refresh.onecake.member.domain.common

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderSheetRepository : JpaRepository<OrderSheet, Long>{
}