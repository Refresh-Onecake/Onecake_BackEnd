package refresh.onecake.review.service

import org.springframework.stereotype.Service
import refresh.onecake.common.response.DefaultResponseDto
import refresh.onecake.review.dto.PostReview
import refresh.onecake.orderhistory.dto.ReviewAndNum
import refresh.onecake.orderhistory.dto.ReviewThumbnail
import refresh.onecake.member.service.SecurityUtil
import refresh.onecake.member.repository.MemberRepository
import refresh.onecake.orderhistory.repository.OrderHistoryRepository
import refresh.onecake.orderhistory.entity.OrderState
import refresh.onecake.review.entity.Review
import refresh.onecake.review.repository.ReviewRepository
import java.time.LocalDateTime

@Service
class ReviewService (
    private val orderHistoryRepository: OrderHistoryRepository,
    private val reviewRepository: ReviewRepository,
    private val memberRepository: MemberRepository
){

    fun getAllReviewsOfSpecificStore(storeId: Long): ReviewAndNum {
        val reviews = reviewRepository.findAllByStoreId(storeId)
        val outputs: MutableList<ReviewThumbnail> = mutableListOf()

        for (i in reviews?.indices!!) {
            val member = memberRepository.getById(reviews[i].consumerId)
            val timeHistory = calculateTimeDiff(reviews[i].createdAt)

            outputs.add(
                ReviewThumbnail(
                    profileImg = member.profileImg,
                    userName = member.userName,
                    timeHistory = timeHistory,
                    content = reviews[i].content,
                )
            )
        }
        return ReviewAndNum(
            reviewNum = outputs.size,
            reviews = outputs
        )
    }

    fun calculateTimeDiff(time: LocalDateTime) : String {
        val now = LocalDateTime.now()
        val timeHistory: String = if (now.year - time.year != 0) {
            (now.year - time.year).toString() + "년 전"
        } else if (now.month.value - time.month.value != 0) {
            (now.month.value - time.month.value).toString() + "달 전"
        } else if (now.dayOfMonth - time.dayOfMonth != 0) {
            (now.dayOfMonth - time.dayOfMonth).toString() + "일 전"
        } else if (now.hour - time.hour != 0) {
            (now.hour - time.hour).toString() + "시간 전"
        } else if (now.minute - time.minute != 0) {
            (now.minute - time.minute).toString() + "분 전"
        } else {
            "1분 전"
        }
        return timeHistory
    }

    fun postReview(postReview: PostReview): DefaultResponseDto {
        val orderHistory = orderHistoryRepository.findOrderHistoryById(postReview.orderId)
        if (reviewRepository.existsByStoreIdAndConsumerIdAndMenuId(
                orderHistory.storeId,
                SecurityUtil.getCurrentMemberId(),
                orderHistory.menuId
            )
        ) {
            return DefaultResponseDto(false, "이미 해당 가게의 메뉴에 리뷰를 작성하였습니다.")
        }
        if (orderHistory.state != OrderState.COMPLETED) {
            return DefaultResponseDto(false, "해당 주문서는 픽업완료 상태가 아닙니다.")
        }
        reviewRepository.save(
            Review(
                id = orderHistory.id,
                consumerId = SecurityUtil.getCurrentMemberId(),
                storeId = orderHistory.storeId,
                menuId = orderHistory.menuId,
                content = postReview.content,
                image = postReview.image,
                price = postReview.price,
                orderHistoryId = orderHistory.id
            )
        )
        return DefaultResponseDto(true, "리뷰 작성을 완료하였습니다.")
    }
}