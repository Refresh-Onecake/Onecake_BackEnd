package refresh.onecake.member.domain.member

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import refresh.onecake.member.domain.ModifyTime
import javax.persistence.*

@EntityListeners(AuditingEntityListener::class)
@Entity
@Table(name = "member")
class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id", nullable = false)
    val id: Long = 0,

    @Column(name = "userName", nullable = false)
    var userName: String,

    @Column(name = "userId", nullable = false)
    var userId: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "isCustomer", nullable = false)
    var isCustomer: Boolean = false,

    @Column(name = "isSeller", nullable = false)
    var isSeller: Boolean = false,

    @Column(name = "phoneNumber", nullable = false)
    val phoneNumber: String,


    @Embedded
    val modifyTime: ModifyTime = ModifyTime()

){

}