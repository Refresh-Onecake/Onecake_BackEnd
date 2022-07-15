package refresh.onecake.member.domain.common

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository : JpaRepository<Question, Long>{
    fun findAllByMenuId(menuId: Long): List<Question>
    fun findQuestionById(id: Long): Question
    fun findByMenuId(menuId: Long): Question?
    fun deleteAllByMenuId(menuId: Long)
}