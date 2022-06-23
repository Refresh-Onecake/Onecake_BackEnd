package refresh.onecake.member.application

import org.springframework.stereotype.Service
import refresh.onecake.member.adapter.api.dto.StoreMainInfoDto
import refresh.onecake.member.domain.seller.StoreRepository

@Service
class ConsumerService (
    private val storeRepository: StoreRepository
){

    fun storeMainInfo(storeId:Long): StoreMainInfoDto {
        val store = storeRepository.getById(storeId)
        return StoreMainInfoDto(storeImage = store.storeImage,
                                storeName = store.storeName,
                                storeDescription = store.storeDiscription,
                                likedNum = 0,
                                reviewNum = 0)
    }

}