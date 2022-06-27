package refresh.onecake.member.domain.seller

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import refresh.onecake.member.adapter.api.dto.MenuIdAndSizeDto

@Repository
interface MenuRepository : JpaRepository<Menu, Long>{

    fun getIdByStoreId(id:Long): Long
    fun existsByMenuSize(menuSize: String): Boolean
    fun findAllByStoreIdOrderByMenuSizeAsc(storeId: Long): List<Menu>?
    fun findAllIdAndMenuSizeByStoreIdOrderByMenuSizeAsc(storeId: Long): List<MenuIdAndSizeDto>?
}