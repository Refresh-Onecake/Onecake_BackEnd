package refresh.onecake.member.domain.common

import javax.persistence.*

@Entity
class Question (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    var menuId: Long,

    var question: String,

    var isConsumerInput: Boolean

){
}