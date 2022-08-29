package refresh.onecake.store.domain

import refresh.onecake.address.domain.Address
import refresh.onecake.member.domain.BaseTimeEntity
import javax.persistence.*

@Entity
class Store(

    @Id
    val id: Long,

    var storeName: String,

    var businessRegistrationNumber: String,

    var storeImage: String,

    @OneToOne
    @JoinColumn(name = "address_id")
    var address: Address,

    var storePhoneNumber: String,

    var storeDiscription: String,

    var openTime: String,

    var closeTime: String,

    var kakaoChannelUrl: String,

    var isActivated: Boolean

) : BaseTimeEntity()