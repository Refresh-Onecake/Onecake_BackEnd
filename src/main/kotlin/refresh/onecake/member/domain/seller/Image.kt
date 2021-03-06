package refresh.onecake.member.domain.seller

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

)