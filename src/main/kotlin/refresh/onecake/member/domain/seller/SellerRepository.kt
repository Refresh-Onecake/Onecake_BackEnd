package refresh.onecake.member.domain.seller

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SellerRepository: JpaRepository<Seller, Long> {
//    fun findByUserId(userId:String): Seller?
//    fun existsByUserId(suerId:String) : Boolean
}