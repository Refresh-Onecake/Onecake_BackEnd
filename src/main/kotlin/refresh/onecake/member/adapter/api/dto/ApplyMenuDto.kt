package refresh.onecake.member.adapter.api.dto


data class ApplyMenuDto(
    var cakeSize:String,
    var cakeImage:String,
    var cakePrice:Int,
    var cakeDescription:String,
    var cakeTaste:String,
    var consumerInput:List<String>,
    var cakeInput:List<String>
)
