package refresh.onecake.member.domain

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

@EntityListeners(AuditingEntityListener::class)
@Entity
class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    var userName: String,

    var userId: String,

    var password: String,

    val phoneNumber: String,

    var profileImg: String?,

    @Enumerated(EnumType.STRING)
    var memberType: MemberType,

    var isActivated: Boolean

) : BaseTimeEntity()