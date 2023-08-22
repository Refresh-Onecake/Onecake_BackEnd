package refresh.onecake.ordersheet.service

import org.springframework.stereotype.Service
import refresh.onecake.ordersheet.dto.AnswersDto
import refresh.onecake.common.response.DefaultResponseDto
import refresh.onecake.ordersheet.dto.OrderSheetTwoTypeDto
import refresh.onecake.member.service.SecurityUtil
import refresh.onecake.member.repository.MemberRepository
import refresh.onecake.menu.repository.MenuRepository
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
    private val memberRepository: MemberRepository,
    private val menuRepository: MenuRepository
){


    fun getOrderSheet(storeId: Long, menuId: Long): OrderSheetTwoTypeDto? {
        val menu = menuRepository.findMenuById(menuId)
        val questions = questionRepository.findAllByMenuIdAndIsActivated(menuId, true)
        return OrderSheetTwoTypeDto(
            menu.menuName,
            consumerInput = questions.filter { it.isConsumerInput && it.isActivated }
                .map { question -> question.question },
            cakeInput = questions.filter { !it.isConsumerInput && it.isActivated }
                .map { question -> question.question },
                menu.price
        )
    }

    fun postOrderSheet(storeId: Long, menuId: Long, answersDto: AnswersDto): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val orderHistory = OrderHistory(
            userId = id,
            storeId = storeId,
            menuId = menuId,
            state = OrderState.RECEIVED,
            pickUpDay = answersDto.date,
            pickUpTime = answersDto.time,
            memo = null,
            reasonForCanceled = null,
        )
        val savedOrderHistory = orderHistoryRepository.save(orderHistory)

        val questions = questionRepository.findAllByMenuIdAndIsActivated(menuId, true)

//        for (i in questions.indices) {
//            val orderSheet = OrderSheet(
//                questionId = questions[i].id,
//                orderId = savedOrderHistory.id,
//                answer = answersDto.answers[i]
//            )
//            orderSheetRepository.save(orderSheet)
//        }
        val orderSheet1 = OrderSheet(
                questionId = questions[0].id,
                orderId = savedOrderHistory.id,
                answer = answersDto.date
        )
        orderSheetRepository.save(orderSheet1)
        val orderSheet2 = OrderSheet(
                questionId = questions[1].id,
                orderId = savedOrderHistory.id,
                answer = answersDto.time
        )
        orderSheetRepository.save(orderSheet2)
        val orderSheet3 = OrderSheet(
                questionId = questions[2].id,
                orderId = savedOrderHistory.id,
                answer = answersDto.name
        )
        orderSheetRepository.save(orderSheet3)
        val orderSheet4 = OrderSheet(
                questionId = questions[3].id,
                orderId = savedOrderHistory.id,
                answer = answersDto.phoneNumber
        )
        orderSheetRepository.save(orderSheet4)
        val orderSheet5 = OrderSheet(
                questionId = questions[4].id,
                orderId = savedOrderHistory.id,
                answer = answersDto.taste
        )
        orderSheetRepository.save(orderSheet5)
        val orderSheet6 = OrderSheet(
                questionId = questions[5].id,
                orderId = savedOrderHistory.id,
                answer = answersDto.background
        )
        orderSheetRepository.save(orderSheet6)
        val orderSheet7 = OrderSheet(
                questionId = questions[6].id,
                orderId = savedOrderHistory.id,
                answer = answersDto.color
        )
        orderSheetRepository.save(orderSheet7)
        val orderSheet8 = OrderSheet(
                questionId = questions[7].id,
                orderId = savedOrderHistory.id,
                answer = answersDto.lettering
        )
        orderSheetRepository.save(orderSheet8)

        val member = memberRepository.getById(id).userName

        return DefaultResponseDto(true, member + "님의 주문이 성공적으로 접수되었습니다!")
    }
}