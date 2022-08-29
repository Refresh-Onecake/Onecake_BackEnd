package refresh.onecake.ordersheet.application

import org.springframework.stereotype.Service
import refresh.onecake.ordersheet.adapter.infra.dto.AnswersDto
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.ordersheet.adapter.infra.dto.OrderSheetTwoTypeDto
import refresh.onecake.member.application.SecurityUtil
import refresh.onecake.member.domain.MemberRepository
import refresh.onecake.orderhistory.domain.OrderHistory
import refresh.onecake.orderhistory.domain.OrderHistoryRepository
import refresh.onecake.orderhistory.domain.OrderState
import refresh.onecake.ordersheet.domain.OrderSheet
import refresh.onecake.ordersheet.domain.OrderSheetRepository
import refresh.onecake.question.domain.QuestionRepository

@Service
class OrderSheetService (
    private val orderHistoryRepository: OrderHistoryRepository,
    private val questionRepository: QuestionRepository,
    private val orderSheetRepository: OrderSheetRepository,
    private val memberRepository: MemberRepository
){


    fun getOrderSheet(storeId: Long, menuId: Long): OrderSheetTwoTypeDto? {
        val questions = questionRepository.findAllByMenuIdAndIsActivated(menuId, true)
        return OrderSheetTwoTypeDto(
            consumerInput = questions.filter { it.isConsumerInput && it.isActivated }
                .map { question -> question.question },
            cakeInput = questions.filter { !it.isConsumerInput && it.isActivated }
                .map { question -> question.question }
        )
    }

    fun postOrderSheet(storeId: Long, menuId: Long, answersDto: AnswersDto): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val orderHistory = OrderHistory(
            userId = id,
            storeId = storeId,
            menuId = menuId,
            state = OrderState.RECEIVED,
            pickUpDay = answersDto.answers[0],
            pickUpTime = answersDto.answers[1],
            memo = null,
            reasonForCanceled = null,
        )
        val savedOrderHistory = orderHistoryRepository.save(orderHistory)

        val questions = questionRepository.findAllByMenuIdAndIsActivated(menuId, true)

        for (i in questions.indices) {
            val orderSheet = OrderSheet(
                questionId = questions[i].id,
                orderId = savedOrderHistory.id,
                answer = answersDto.answers[i]
            )
            orderSheetRepository.save(orderSheet)
        }

        val member = memberRepository.getById(id).userName

        return DefaultResponseDto(true, member + "님의 주문이 성공적으로 접수되었습니다!")
    }
}