package refresh.onecake.member.entity

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import refresh.onecake.common.BaseTimeEntity
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

    var phoneNumber: String,

    var profileImg: String?,

    @Enumerated(EnumType.STRING)
    var memberType: MemberType,

    var isActivated: Boolean,

    val fcmToken: String

) : BaseTimeEntity()