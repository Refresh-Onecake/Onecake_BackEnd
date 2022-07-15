package refresh.onecake.member.domain.seller

import refresh.onecake.member.domain.BaseTimeEntity
import javax.persistence.*

@Entity
class Menu (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1L,

    var storeId: Long,

    var menuName: String,

    var menuSize: String,

    var price: Int,

    var menuDescription: String,

    var taste: String,

    var image: String

) : BaseTimeEntity()