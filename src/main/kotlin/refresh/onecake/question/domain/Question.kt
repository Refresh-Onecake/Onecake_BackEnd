package refresh.onecake.question.domain

import javax.persistence.*

@Entity
@Table(indexes = [Index(name = "index_menu_id", columnList = "menuId")])
class Question (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    var menuId: Long,

    var question: String,

    var isConsumerInput: Boolean,

    var isActivated: Boolean

){
}