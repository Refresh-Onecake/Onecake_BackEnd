package refresh.onecake.member.domain.common

import refresh.onecake.member.domain.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Order (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    var userId:Long,

    var storeId:Long,

    var menuId:Long,

    var state:OrderState,

) : BaseTimeEntity()