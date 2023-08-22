package refresh.onecake.chat.service

import org.springframework.stereotype.Service
import refresh.onecake.chat.entity.Chat
import refresh.onecake.chat.repository.ChatRepository
import refresh.onecake.common.response.DefaultResponseDto
import refresh.onecake.member.service.SecurityUtil
import refresh.onecake.store.repository.StoreRepository

@Service
class ChatService(
        val chatRepository: ChatRepository,
        val storeRepository: StoreRepository
) {

    fun postChat(storeId: Long) : DefaultResponseDto{
        val id = SecurityUtil.getCurrentMemberId()
        val chat = Chat(consumerId = id, storeId = storeId)
        if(!chatRepository.existsByConsumerIdAndStoreId(id, storeId)) chatRepository.save(chat)
        return DefaultResponseDto(true, "성공적으로 저장하였습니다.")
    }

    fun getChats() : List<ChatStoreInfo> {
        val id = SecurityUtil.getCurrentMemberId()
        val chats = chatRepository.findAllByConsumerIdOrderByCreatedAtDesc(id)
        println(chats)
        val response = mutableListOf<ChatStoreInfo>()
        for(chat in chats){
            val store = storeRepository.findStoreById(chat.storeId)
            response.add(ChatStoreInfo(store.storeImage, store.storeName, store.address.roadFullAddr!!, store.kakaoChannelUrl))
        }
        return response
    }
}

data class ChatStoreInfo(
        val storeImg: String,
        val storeName: String,
        val storeLocation: String,
        val kakaoUrl: String
)