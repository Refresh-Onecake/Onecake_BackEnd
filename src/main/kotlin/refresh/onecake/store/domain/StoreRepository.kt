package refresh.onecake.store.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository: JpaRepository<Store, Long> {
    fun findStoreById(id:Long): Store
    fun findByAddressIdAndIsActivated(addressId: Long, isActivated: Boolean): Store?
}