package refresh.onecake.member.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SellerRepository: JpaRepository<Seller, Long> {
}