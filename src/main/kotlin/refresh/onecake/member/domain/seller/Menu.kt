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

    var menuSize: String?,

    @ElementCollection
    var image: MutableList<String>?,

    @Enumerated(value = EnumType.STRING)
    var keyword: Keyword?,

    var price: Int?,

    var menuDescription: String?,

    var taste: String?

) : BaseTimeEntity()