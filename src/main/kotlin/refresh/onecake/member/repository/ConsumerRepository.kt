package refresh.onecake.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import refresh.onecake.member.entity.Consumer

@Repository
interface ConsumerRepository: JpaRepository<Consumer, Long> {
//    fun findByUserId(userId:String): Consumer?
//    fun existsByUserId(suerId:String) : Boolean
}