package refresh.onecake.chat.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import refresh.onecake.chat.entity.Chat

@Repository
interface ChatRepository : JpaRepository<Chat, Long> {
    fun findAllByConsumerIdOrderByCreatedAtDesc(consumerId: Long): List<Chat>
    fun existsByConsumerIdAndStoreId(consumerId: Long, storeId: Long): Boolean
}