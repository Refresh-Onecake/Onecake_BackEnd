package refresh.onecake.member.adapter.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class ApplyStoreRequestDto (
    var storeName:String?,
    var businessRegistrationNumber:String?,
    var address: AddressRequestDto?,
    var storePhoneNumber: String?,
    var storeDiscription: String?,
    var openTime: String?,
    var closeTime: String?,
    var kakaoChannelUrl: String?
)