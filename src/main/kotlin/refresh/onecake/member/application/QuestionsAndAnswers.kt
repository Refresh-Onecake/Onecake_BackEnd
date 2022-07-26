package refresh.onecake.member.application

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import refresh.onecake.member.domain.common.OrderSheetRepository
import refresh.onecake.member.domain.common.QuestionRepository

@Component
class QuestionsAndAnswers(
    private val orderSheetRepository: OrderSheetRepository,
    private val questionRepository: QuestionRepository
) {

    fun getQuestionsAndAnswers(orderHistoryId: Long): List<String> {
        val orderSheet = orderSheetRepository.findAllByOrderId(orderHistoryId)
        val answers = orderSheet?.map { it.answer }
        val questionIds = orderSheet?.map { it.questionId }

        var forms: MutableList<String> = mutableListOf()
        for (i in questionIds?.indices!!) {
            forms.add(i, questionRepository.findQuestionById(questionIds[i]).question + " : " + answers?.get(i))
        }
        return forms
    }

}