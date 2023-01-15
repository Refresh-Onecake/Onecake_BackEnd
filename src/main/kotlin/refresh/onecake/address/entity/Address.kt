package refresh.onecake.address.entity

import refresh.onecake.common.BaseTimeEntity
import javax.persistence.*

@Entity
class Address (

    @Id
    val id: Long,

    var jibunAddress:String?,
    var roadFullAddr:String?,
    var siNm:String?,
    var sggNm:String?,
    var emdNm:String?,
    var lnbrMnnm:String?,
    var addressDetail:String?,

//    jibunAddress : 지번주소
//    roadFullAddr : 도로명주소
//    siNm : 시도명
//    sggNm : 시군구명
//    emdNm : 읍면동명
//    lnbrMnnm : 지번본번
//    addressDetail : 고객 입력 상세 주소
) : BaseTimeEntity()