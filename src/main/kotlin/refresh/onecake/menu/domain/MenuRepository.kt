package refresh.onecake.menu.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import refresh.onecake.menu.adapter.infra.dto.MenuIdAndSizeDto
import refresh.onecake.menu.adapter.infra.dto.MenuIdAndStoreId

@Repository
interface MenuRepository : JpaRepository<Menu, Long>{
    fun findMenuById(id: Long): Menu
    fun findAllByStoreIdAndIsActivatedOrderByMenuNameAsc(id:Long, isActivate: Boolean): List<Menu>
    fun findAllByStoreIdOrderByMenuSizeAsc(storeId: Long): List<Menu>?
    fun findAllIdAndMenuSizeByStoreIdAndIsActivatedOrderByMenuSizeAsc(storeId: Long, isActivate: Boolean): List<MenuIdAndSizeDto>?
    fun existsByMenuSizeAndStoreId(menuSize: String, storeId: Long): Boolean
    fun existsByMenuSizeAndStoreIdAndIsActivated(menuSize: String, storeId: Long, isActivate: Boolean): Boolean
    fun findByMenuSizeAndStoreIdAndIsActivated(menuSize: String, storeId: Long, isActivate: Boolean): Menu?

    @Query(nativeQuery = true, value = """
        select m.id as menuId, s.id as storeId 
        from menu m, store s 
        where m.store_id = s.id and m.id = ?1
    """)
    fun findMenuWithStore(menuId:Long): MenuIdAndStoreId
}