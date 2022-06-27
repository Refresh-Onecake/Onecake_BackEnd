package refresh.onecake.member.domain.seller

import javax.persistence.*

@Entity
class Image (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    @ManyToOne(targetEntity=Menu::class, fetch= FetchType.LAZY)
    @JoinColumn(name="menu_id")
    var menu: Menu,

    @Enumerated(value = EnumType.STRING)
    var keyword: Keyword?,

)