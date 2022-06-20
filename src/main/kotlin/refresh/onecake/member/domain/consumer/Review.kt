package refresh.onecake.member.domain.consumer

import javax.persistence.*

@Entity
class Review (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REVIEW_ID")
    var consumer: Consumer

)