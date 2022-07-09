package refresh.onecake.member.domain.consumer

import javax.persistence.*

@Entity
class Review (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    var consumerId: Long,

    var storeId: Long,

    var menuId: Long,

    var content: String,

    var image: String,

    var price: String

)