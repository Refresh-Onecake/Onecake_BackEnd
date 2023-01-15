package refresh.onecake.ordersheet.service

import org.springframework.stereotype.Service
import refresh.onecake.ordersheet.dto.AnswersDto
import refresh.onecake.common.response.DefaultResponseDto
import refresh.onecake.ordersheet.dto.OrderSheetTwoTypeDto
import refresh.onecake.member.service.SecurityUtil
import refresh.onecake.member.repository.MemberRepository
import refresh.onecake.orderhistory.entity.OrderHistory
import refresh.onecake.orderhistory.repository.OrderHistoryRepository
import refresh.onecake.orderhistory.entity.OrderState
import refresh.onecake.ordersheet.entity.OrderSheet
import refresh.onecake.ordersheet.repository.OrderSheetRepository
import refresh.onecake.question.repository.QuestionRepository

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