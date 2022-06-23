package refresh.onecake.member.adapter.api.dto

data class menuRegistrationDto(
    var cakeSize: String?,
    var cakePrice: String?,
    var cakeDescription: String?,
    var cakeTaste: String?,
    var consumerInputs: List<ConsumerInputDto>?,
    var cakeInputs: List<ConsumerInputDto>?
)
