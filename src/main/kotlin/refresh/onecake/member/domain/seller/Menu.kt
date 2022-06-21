package refresh.onecake.member.domain.seller

import refresh.onecake.member.domain.BaseTimeEntity
import javax.persistence.*

@Entity
class Menu (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    @ManyToOne(targetEntity=Store::class, fetch= FetchType.LAZY)
    @JoinColumn(name="store_id")
    var store: Store,

    var menuName: String?,

    var menuImage: String?,

    var price: Integer?,

    var menuDescription: String?,





) : BaseTimeEntity()