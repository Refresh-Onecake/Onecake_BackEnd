package refresh.onecake.member.domain.seller

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class DayOff (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    val storeId: Long,

    val day: String,

    val dayOffName: String
){
}