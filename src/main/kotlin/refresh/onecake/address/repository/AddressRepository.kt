package refresh.onecake.address.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import refresh.onecake.address.entity.Address

@Repository
interface AddressRepository : JpaRepository<Address, Long>{
    fun findAllBySggNm(sggNm: String): List<Address>?

}