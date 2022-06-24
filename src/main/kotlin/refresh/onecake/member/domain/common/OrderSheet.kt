package refresh.onecake.member.domain.common

import refresh.onecake.member.domain.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class OrderSheet (

    @Id
    val id:Long, // question Id와 동일

    var orderId:Long,

    var answer:String

) : BaseTimeEntity()