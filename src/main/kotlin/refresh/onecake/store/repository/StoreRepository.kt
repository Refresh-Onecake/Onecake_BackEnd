package refresh.onecake.store.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import refresh.onecake.store.entity.Store

@Repository
interface StoreRepository: JpaRepository<Store, Long> {
    fun findStoreById(id:Long): Store
//    fun findByAddressIdAndIsActivated(addressId: Long, isActivated: Boolean): Store?
//    fun findAll() : List<Store>
}