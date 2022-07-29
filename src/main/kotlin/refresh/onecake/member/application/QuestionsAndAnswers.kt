package refresh.onecake.member.application

import org.springframework.stereotype.Component
import refresh.onecake.member.domain.common.OrderSheetRepository

@Component
class QuestionsAndAnswers(
    private val orderSheetRepository: OrderSheetRepository
) {

    fun getQuestionsAndAnswers(orderHistoryId: Long): List<String> {
        val questionAndAnswer = orderSheetRepository.getBundleOfQuestionAndAnswerWhoseConsumerInputIsFalse(orderHistoryId)
        var forms = mutableListOf<String>()
        for (i in questionAndAnswer.indices) {
            forms.add(i, questionAndAnswer[i].question + " : " +questionAndAnswer[i].answer)
        }
        return forms
    }

}