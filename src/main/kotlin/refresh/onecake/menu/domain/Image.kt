package refresh.onecake.menu.domain

import refresh.onecake.member.domain.BaseTimeEntity
import javax.persistence.*

@Entity
class Image (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    var menuId: Long,

    var image: String,

    @Enumerated(value = EnumType.STRING)
    var keyword: Keyword?,

    var likeNum: Long,

    var isActivated: Boolean

) : BaseTimeEntity()