package refresh.onecake.review.entity

import refresh.onecake.common.BaseTimeEntity
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

    var price: String,

    val orderHistoryId: Long

): BaseTimeEntity()