package refresh.onecake.member.application

import org.apache.tomcat.jni.Local
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import refresh.onecake.member.adapter.api.dto.*
import refresh.onecake.member.adapter.infra.ForbiddenException
import refresh.onecake.member.application.util.SecurityUtil
import refresh.onecake.member.domain.common.*
import refresh.onecake.member.domain.consumer.Review
import refresh.onecake.member.domain.consumer.ReviewRepository
import refresh.onecake.member.domain.consumer.StoreLike
import refresh.onecake.member.domain.consumer.StoreLikeRepository
import refresh.onecake.member.domain.member.MemberRepository
import refresh.onecake.member.domain.seller.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
class ConsumerService (
    private val memberRepository: MemberRepository,
    private val storeRepository: StoreRepository,
    private val menuRepository: MenuRepository,
    private val addressRepository: AddressRepository,
    private val questionRepository: QuestionRepository,
    private val orderHistoryRepository: OrderHistoryRepository,
    private val orderSheetRepository: OrderSheetRepository,
    private val dayOffRepository: DayOffRepository,
    private val storeLikeRepository: StoreLikeRepository,
    private val reviewRepository: ReviewRepository,
    private val imageRepository: ImageRepository,
    private val questionsAndAnswers: QuestionsAndAnswers,
    private val modelMapper: ModelMapper
){

    fun storeMainInfo(storeId:Long): StoreMainInfoDto {
        val id = SecurityUtil.getCurrentMemberId()
        val store = storeRepository.getById(storeId)

        isActivatedStore(store)

        val address = addressRepository.getById(storeId)
        val temp = store.storeName.elementAt(store.storeName.length - 1)
        val index = (temp - 0xAC00.toChar()) % 28
        val description = if (temp < 0xAC00.toChar() || temp > 0xD7A3.toChar()) {
            address.sggNm + "에 위치한 " + store.storeName + "이에요."
        } else if (index > 0) {
            address.sggNm + "에 위치한 " + store.storeName + "이에요."
        } else {
            address.sggNm + "에 위치한 " + store.storeName + "에요."
        }

        return StoreMainInfoDto(
            storeImage = store.storeImage,
            storeName = store.storeName,
            storeDescription = description,
            likeNum = storeLikeRepository.countByStoreId(storeId),
            isLiked = storeLikeRepository.existsByMemberIdAndStoreId(id, storeId)
        )
    }

    fun storeMenuList(storeId: Long): List<StoreMenuListDto>? {
        isActivatedStore(storeRepository.findStoreById(storeId))
        return menuRepository.findAllByStoreIdAndIsActivatedOrderByMenuNameAsc(storeId, true)
            .map { modelMapper.map(it, StoreMenuListDto::class.java) }
    }

    fun getStoreInformation(storeId: Long): StoreDetailInfoDto {
        isActivatedStore(storeRepository.findStoreById(storeId))
        val store = storeRepository.getById(storeId)
        return StoreDetailInfoDto(
            operatingTime = store.openTime + " ~ " + store.closeTime,
            dayOff = "주문 시 확인",
            address = addressRepository.getById(storeId).roadFullAddr.orEmpty(),
            storeDescription = store.storeDiscription
        )
    }

    fun getCakesSize(storeId: Long): StoreNameAndCakeSizesDto{
        isActivatedStore(storeRepository.findStoreById(storeId))
        return StoreNameAndCakeSizesDto(
            storeName = storeRepository.findStoreById(storeId).storeName,
            sizes = menuRepository.findAllIdAndMenuSizeByStoreIdAndIsActivatedOrderByMenuSizeAsc(storeId, true)
                ?.map{modelMapper.map(it, MenuIdAndSizeDto::class.java)}
        )
    }

    fun getAllImagesOfSpecificMenu(storeId: Long, menuId: Long): MenuDescAndImages {
        val images = imageRepository.findAllByMenuId(menuId)
        val store = storeRepository.findStoreById(storeId)
        isActivatedStore(store)
        val menu = menuRepository.findMenuById(menuId)

        return MenuDescAndImages(
            storeName = store.storeName,
            menuName = menu.menuName,
            menuDescription = menu.menuDescription,
            allImages = images.map { modelMapper.map(it, MenuIdAndImage::class.java) },
            birthdayImages = images.filter { it.keyword == Keyword.BIRTHDAY }.map { modelMapper.map(it, MenuIdAndImage::class.java) },
            monthlyEventImages = images.filter { it.keyword == Keyword.MONTHLY_EVENT }.map { modelMapper.map(it, MenuIdAndImage::class.java) },
            anniversaryImages = images.filter { it.keyword == Keyword.ANNIVERSARY }.map { modelMapper.map(it, MenuIdAndImage::class.java) },
            employmentImages = images.filter { it.keyword == Keyword.EMPLOYMENT }.map { modelMapper.map(it, MenuIdAndImage::class.java) },
            marriageImages = images.filter { it.keyword == Keyword.MARRIAGE }.map { modelMapper.map(it, MenuIdAndImage::class.java) },
            dischargeImages = images.filter { it.keyword == Keyword.DISCHARGE }.map { modelMapper.map(it, MenuIdAndImage::class.java) }
        )
    }

    fun getOrderSheet(storeId: Long, menuId: Long): OrderSheetTwoTypeDto? {
        isActivatedStore(storeRepository.findStoreById(storeId))
        val questions = questionRepository.findAllByMenuIdAndIsActivated(menuId, true)
        return OrderSheetTwoTypeDto(
            consumerInput = questions?.filter { it.isConsumerInput && it.isActivated }?.map { question -> question.question },
            cakeInput = questions?.filter { !it.isConsumerInput && it.isActivated }?.map { question -> question.question }
        )
    }

    fun getStoreDayOffs(storeId: Long) : List<String>? {
        return dayOffRepository.findAllByStoreId(storeId)?.map { it.day }
    }

    fun postOrderSheet(storeId: Long, menuId: Long, answersDto: AnswersDto): DefaultResponseDto {
        isActivatedStore(storeRepository.findStoreById(storeId))
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

    fun pushStoreLike(storeId:Long): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        isActivatedStore(storeRepository.findStoreById(storeId))
        val storeLike = storeLikeRepository.findStoreLikeByMemberIdAndStoreId(id, storeId)
        return if (storeLike != null) {
            storeLikeRepository.delete(storeLike)
            DefaultResponseDto(true,"가게 좋아요를 취소하였습니다.")
        } else {
            storeLikeRepository.save(StoreLike(
                memberId = id,
                storeId = storeId
            ))
            DefaultResponseDto(true, "가게 좋아요를 추가하였습니다.")
        }
    }

    fun getAllStoreByAddressAndFilter(addressAndFilter: AddressAndFilter): List<StoreThumbNail>? {
        val id = SecurityUtil.getCurrentMemberId()
        val addressId: List<Long>? = addressRepository.findAllBySggNm(addressAndFilter.address)?.map { it.id }
        val output: MutableList<StoreThumbNail> = mutableListOf()
        for (i in addressId?.indices!!) {
            val store = storeRepository.findByAddressIdAndIsActivated(addressId[i], true) ?: continue
            output.add(StoreThumbNail(
                storeId = store.id,
                storeImage = store.storeImage,
                guName = addressRepository.getById(addressId[i]).sggNm!!,
                storeName = store.storeName,
                likedNum = storeLikeRepository.countByStoreId(store.id),
                reviewNum = reviewRepository.countByStoreId(store.id),
                isLiked = storeLikeRepository.existsByMemberIdAndStoreId(id, store.id)
            ))
        }
        if (addressAndFilter.filter == "review") {
            output.sortByDescending { it.reviewNum }
        } else {
            output.sortByDescending { it.likedNum }
        }
        return output
    }

    fun postReview(postReview: PostReview): DefaultResponseDto {
        val orderHistory = orderHistoryRepository.findOrderHistoryById(postReview.orderId)
        if (reviewRepository.existsByStoreIdAndConsumerIdAndMenuId(
                orderHistory.storeId,
                SecurityUtil.getCurrentMemberId(),
                orderHistory.menuId
            )
        ) {
            throw ForbiddenException("이미 해당 가게의 메뉴에 리뷰를 작성하였습니다.")
        }
        if (orderHistory.state != OrderState.COMPLETED) {
            throw ForbiddenException("해당 주문서는 픽업완료 상태가 아닙니다.")
        }
        reviewRepository.save(Review(
            id = orderHistory.id,
            consumerId = SecurityUtil.getCurrentMemberId(),
            storeId = orderHistory.storeId,
            menuId = orderHistory.menuId,
            content = postReview.content,
            image = postReview.image,
            price = postReview.price,
            orderHistoryId = orderHistory.id
        ))
        return DefaultResponseDto(true, "리뷰 작성을 완료하였습니다.")
    }

    fun getAllReviewsOfSpecificStore(storeId: Long): ReviewAndNum{
        isActivatedStore(storeRepository.findStoreById(storeId))
        val reviews = reviewRepository.findAllByStoreId(storeId)
        val outputs: MutableList<ReviewThumbnail> = mutableListOf()
        for (i in reviews?.indices!!) {

            val member = memberRepository.getById(reviews[i].consumerId)
            val timeHistory = calculateTimeDiff(reviews[i].createdAt)

            outputs.add(ReviewThumbnail(
                profileImg = member.profileImg,
                userName = member.userName,
                timeHistory = timeHistory,
                content = reviews[i].content,
            ))
        }
        return ReviewAndNum(
            reviewNum = outputs.size,
            reviews = outputs
        )
    }

    fun calculateTimeDiff(time: LocalDateTime) : String{
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

    fun isActivatedStore(store: Store){
        if(!store.isActivated) throw ForbiddenException("접근할 수 없는 가게입니다.")
    }

    fun getOrderHistorys(): List<MyOrderHistorys> {
        val id = SecurityUtil.getCurrentMemberId()
        val orderHistorys = orderHistoryRepository.findAllByUserId(id)
        val outputs: MutableList<MyOrderHistorys> = mutableListOf()
        for (i in orderHistorys.indices) {
            val orderHistory = orderHistorys[i]
            val store = storeRepository.findStoreById(orderHistory.storeId)
            val menu = menuRepository.findMenuById(orderHistory.menuId)
            outputs.add(
                MyOrderHistorys(
                    orderHistoryId = orderHistory.id,
                    storeName = store.storeName,
                    orderState = orderHistory.state,
                    menuName = menu.menuName,
                    menuPrice = menu.price,
                    menuImage = menu.image,
                    hasReview = reviewRepository.existsByOrderHistoryId(orderHistory.id)
                )
            )
        }
        return outputs
    }

    fun getSpecificOrderHistory(orderHistoryId: Long): SpecificOrderHistory {
        val orderHistory = orderHistoryRepository.findOrderHistoryById(orderHistoryId)
        val store = storeRepository.findStoreById(orderHistory.storeId)
        val menu = menuRepository.findMenuById(orderHistory.menuId)

        val orderState = when (orderHistory.state) {
            OrderState.RECEIVED -> {
                "주문이 사장님께 전달되었어요!"
            }
            OrderState.ACCEPTED -> {
                "결제까지 완료되었어요!"
            }
            OrderState.MAKING -> {
                "케이크를 제작 중이에요!"
            }
            OrderState.COMPLETED -> {
                "픽업이 완료되었어요!"
            }
            else -> {
                "주문이 취소되었습니다(" + orderHistory.reasonForCanceled + ")"
            }
        }

        val orderTime = orderHistory.createdAt

        val pickUpDate = orderHistory.pickUpDay.split("-")
        val pickUpTime = orderHistory.pickUpTime.split(":")
        val ampmOfPickUpTime = if(pickUpTime[0].toInt() > 11) "오후 " else "오전 "
        val hourOfPickUpTime = if(pickUpTime[0].toInt() > 12) pickUpTime[0].toInt() - 12 else pickUpTime[0].toInt()

        val hourOfPickUpTimeString = if(hourOfPickUpTime < 10) "0$hourOfPickUpTime" else hourOfPickUpTime

        return SpecificOrderHistory(
            storeName = store.storeName,
            orderState = orderState,
            orderTime = orderTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a hh:mm")),
            pickUpTime = pickUpDate[0] + "년 " + pickUpDate[1] + "월 " + pickUpDate[2] + "일 " + ampmOfPickUpTime + hourOfPickUpTimeString + ":" + pickUpTime[1],
            menuName = menu.menuName,
            menuPrice = menu.price,
            form = questionsAndAnswers.getQuestionsAndAnswers(orderHistoryId)
        )
    }

}