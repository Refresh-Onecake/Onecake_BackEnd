package refresh.onecake.member.domain.common

import refresh.onecake.member.domain.BaseTimeEntity
import javax.persistence.*

@Entity
class OrderHistory (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    var userId:Long,

    var storeId:Long,

    var menuId:Long,

    @Enumerated(value = EnumType.STRING)
    var state:OrderState,

    var pickUpDay: String,

    var pickUpTime: String,

    var memo: String?,

    var reasonForCanceled: String?,

) : BaseTimeEntity()