package refresh.onecake.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import refresh.onecake.member.entity.Member
import refresh.onecake.member.entity.MemberType

@Repository
interface MemberRepository: JpaRepository<Member, Long> {
    fun findMemberById(id: Long): Member
    fun findByUserId(userId:String): Member
    fun getByUserId(userId:String): Member
    fun existsByUserId(userId:String) : Boolean
    fun findMemberTypeById(id:Long) : MemberType
}