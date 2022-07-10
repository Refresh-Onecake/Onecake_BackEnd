package refresh.onecake.member.domain.consumer

import refresh.onecake.member.domain.BaseTimeEntity
import javax.persistence.*

@Entity
class Review (

    @Id
    val id: Long,

    var consumerId: Long,

    var storeId: Long,

    var menuId: Long,

    var content: String,

    var image: String?,

    var price: String

): BaseTimeEntity()