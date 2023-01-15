package refresh.onecake.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import refresh.onecake.member.entity.Seller

@Repository
interface SellerRepository: JpaRepository<Seller, Long> {
}