package refresh.onecake.member.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConsumerRepository: JpaRepository<Consumer, Long> {
//    fun findByUserId(userId:String): Consumer?
//    fun existsByUserId(suerId:String) : Boolean
}