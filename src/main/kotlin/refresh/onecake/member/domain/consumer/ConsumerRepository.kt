package refresh.onecake.member.domain.consumer

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import refresh.onecake.member.domain.member.Member

@Repository
interface ConsumerRepository: JpaRepository<Consumer, Long> {
//    fun findByUserId(userId:String): Consumer?
//    fun existsByUserId(suerId:String) : Boolean
}