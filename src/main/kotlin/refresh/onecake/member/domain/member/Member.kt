package refresh.onecake.member.domain.member

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import refresh.onecake.member.domain.BaseTimeEntity
import refresh.onecake.member.domain.ModifyTime
import javax.persistence.*

@EntityListeners(AuditingEntityListener::class)
@Entity
@Table(name = "member")
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
    var memberType: MemberType

) : BaseTimeEntity()