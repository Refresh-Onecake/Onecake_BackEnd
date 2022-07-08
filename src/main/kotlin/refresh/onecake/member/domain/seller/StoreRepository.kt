package refresh.onecake.member.domain.seller

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository: JpaRepository<Store, Long> {
    fun findStoreById(id:Long): Store
    fun findByAddressId(addressId: Long): Store
}