package refresh.onecake.member.domain.seller

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import refresh.onecake.member.adapter.api.dto.MenuIdAndSizeDto

@Repository
interface MenuRepository : JpaRepository<Menu, Long>{
    fun findMenuById(id: Long): Menu
    fun findAllByStoreIdAndIsActivatedOrderByMenuNameAsc(id:Long, isActivate: Boolean): List<Menu>
    fun findAllByStoreIdOrderByMenuSizeAsc(storeId: Long): List<Menu>?
    fun findAllIdAndMenuSizeByStoreIdOrderByMenuSizeAsc(storeId: Long): List<MenuIdAndSizeDto>?
    fun existsByMenuSizeAndStoreId(menuSize: String, storeId: Long): Boolean
}