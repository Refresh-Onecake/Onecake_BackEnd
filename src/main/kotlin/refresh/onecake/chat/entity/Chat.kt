package refresh.onecake.chat.entity;

import refresh.onecake.common.BaseTimeEntity
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
class Chat (

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = -1L,

        val consumerId: Long,
        val storeId: Long

) : BaseTimeEntity()
