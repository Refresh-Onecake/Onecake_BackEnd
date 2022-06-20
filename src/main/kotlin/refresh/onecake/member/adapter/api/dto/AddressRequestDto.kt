package refresh.onecake.member.adapter.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class AddressRequestDto(
    var jibunAddress:String?,
    var roadFullAddr:String?,
    var siNm:String?,
    var sggNm:String?,
    var emdNm:String?,
    var lnbrMnnm:String?,
    var addressDetail:String?,
)
