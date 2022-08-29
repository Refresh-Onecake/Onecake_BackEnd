package refresh.onecake.question.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository : JpaRepository<Question, Long>{
    fun findAllByMenuIdAndIsActivated(menuId: Long, isActivated: Boolean): List<Question>
    fun findQuestionById(id: Long): Question
    fun findQuestionByIdAndIsActivated(id: Long, isActivated: Boolean): Question
    fun findByMenuId(menuId: Long): Question?
    fun deleteAllByMenuId(menuId: Long)
}