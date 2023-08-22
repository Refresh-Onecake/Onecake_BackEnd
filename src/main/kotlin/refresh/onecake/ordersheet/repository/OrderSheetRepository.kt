package refresh.onecake.ordersheet.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import refresh.onecake.menu.dto.QuestionAndAnswer
import refresh.onecake.ordersheet.entity.OrderSheet

@Repository
interface OrderSheetRepository : JpaRepository<OrderSheet, Long>{

    @Query(nativeQuery = true, value = """
        select o.answer as answer, q.question as question 
        from order_sheet o, question q 
        where o.question_id = q.id 
        and order_id = ?1 
        and is_consumer_input = false
    """)
    fun getBundleOfQuestionAndAnswerWhoseConsumerInputIsFalse(orderId: Long): List<QuestionAndAnswer>

    @Query(nativeQuery = true, value = """
        select o.answer as answer, q.question as question 
        from order_sheet o, question q 
        where o.question_id = q.id 
                and order_id = ?1
    """)
    fun getBundleOfQuestionAndAnswer(orderId: Long): List<QuestionAndAnswer>
}