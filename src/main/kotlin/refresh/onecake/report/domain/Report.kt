package refresh.onecake.report.domain

import refresh.onecake.member.domain.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Report(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1L,

    val reporterId: Long,

    val storeName: String,

    val reason: String

): BaseTimeEntity()