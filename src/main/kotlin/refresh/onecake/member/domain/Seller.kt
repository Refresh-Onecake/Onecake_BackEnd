package refresh.onecake.member.domain

import refresh.onecake.member.domain.BaseTimeEntity
import refresh.onecake.store.domain.Store
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
class Seller (

    @Id
    val id: Long,

    @OneToOne
    @JoinColumn(name = "store_id")
    var store: Store?,

    ) : BaseTimeEntity()