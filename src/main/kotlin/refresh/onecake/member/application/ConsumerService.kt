package refresh.onecake.member.application

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import refresh.onecake.member.adapter.api.dto.*
import refresh.onecake.member.application.util.SecurityUtil
import refresh.onecake.member.domain.common.*
import refresh.onecake.member.domain.consumer.ReviewRepository
import refresh.onecake.member.domain.consumer.StoreLike
import refresh.onecake.member.domain.consumer.StoreLikeRepository
import refresh.onecake.member.domain.member.MemberRepository
import refresh.onecake.member.domain.seller.*

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
    private val modelMapper: ModelMapper
){

    fun storeMainInfo(storeId:Long): StoreMainInfoDto {
        val id = SecurityUtil.getCurrentMemberId()
        val store = storeRepository.getById(storeId)
        return StoreMainInfoDto(
            storeImage = store.storeImage,
            storeName = store.storeName,
            storeDescription = store.storeDiscription,
            likeNum = storeLikeRepository.countByStoreId(storeId),
            isLiked = storeLikeRepository.existsByMemberIdAndStoreId(id, storeId)
        )
    }

    fun storeMenuList(storeId: Long): List<StoreMenuListDto>? {
        return menuRepository.findAllByStoreIdOrderByMenuSizeAsc(storeId)
            ?.map { modelMapper.map(it, StoreMenuListDto::class.java) }
    }

    fun getStoreInformation(storeId: Long): StoreDetailInfoDto {
        val store = storeRepository.getById(storeId)
        return StoreDetailInfoDto(
            operatingTime = store.openTime + " ~ " + store.closeTime,
            dayOff = "주문 시 확인",
            address = addressRepository.getById(storeId).roadFullAddr.orEmpty(),
            storeDescription = store.storeDiscription
        )
    }

    fun getCakesSize(storeId: Long): StoreNameAndCakeSizesDto{
        return StoreNameAndCakeSizesDto(
            storeName = storeRepository.findStoreById(storeId).storeName,
            sizes = menuRepository.findAllIdAndMenuSizeByStoreIdOrderByMenuSizeAsc(storeId)
                ?.map{modelMapper.map(it, MenuIdAndSizeDto::class.java)}
        )
    }

    fun getAllImagesOfSpecificMenu(storeId: Long, menuId: Long): MenuDescAndImages {
        var images = imageRepository.findAllByMenuId(menuId)
        var store = storeRepository.findStoreById(storeId)
        var menu = menuRepository.findMenuById(menuId)

        return MenuDescAndImages(
            storeName = store.storeName,
            menuName = menu.menuName,
            menuDescription = menu.menuDescription,
            allImages = images?.map { modelMapper.map(it, MenuIdAndImage::class.java) },
            birthdayImages = images?.filter { it.keyword == Keyword.BIRTHDAY }?.map { modelMapper.map(it, MenuIdAndImage::class.java) },
            monthlyEventImages = images?.filter { it.keyword == Keyword.MONTHLY_EVENT }?.map { modelMapper.map(it, MenuIdAndImage::class.java) },
            anniversaryImages = images?.filter { it.keyword == Keyword.ANNIVERSARY }?.map { modelMapper.map(it, MenuIdAndImage::class.java) },
            employmentImages = images?.filter { it.keyword == Keyword.EMPLOYMENT }?.map { modelMapper.map(it, MenuIdAndImage::class.java) },
            marriageImages = images?.filter { it.keyword == Keyword.MARRIAGE }?.map { modelMapper.map(it, MenuIdAndImage::class.java) },
            dischargeImages = images?.filter { it.keyword == Keyword.DISCHARGE }?.map { modelMapper.map(it, MenuIdAndImage::class.java) }
        )
    }

    fun getOrderSheet(storeId: Long, menuId: Long): OrderSheetTwoTypeDto? {
        val questions = questionRepository.findAllByMenuId(menuId)
        return OrderSheetTwoTypeDto(
//            consumerInput = questions?.filter { it.isConsumerInput }?.map { modelMapper.map(it, IdAndQuestionDto::class.java) },
//            cakeInput = questions?.filter { !it.isConsumerInput }?.map { modelMapper.map(it, IdAndQuestionDto::class.java) }
            consumerInput = questions?.filter { it.isConsumerInput }?.map { question -> question.question },
            cakeInput = questions?.filter { !it.isConsumerInput }?.map { question -> question.question }
        )
    }

    fun getStoreDayOffs(storeId: Long) : List<String>? {
        return dayOffRepository.findAllByStoreId(storeId)?.map { it.day }
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

        val questions = questionRepository.findAllByMenuId(menuId)

        if (questions != null) {
            for (i in questions.indices) {
                var orderSheet = OrderSheet(
                    questionId = questions[i].id,
                    orderId = savedOrderHistory.id,
                    answer = answersDto?.answers!![i]
                )
                orderSheetRepository.save(orderSheet)
            }
        }

        val member = memberRepository.getById(id).userName

        return DefaultResponseDto(true, member + "님의 주문이 성공적으로 접수되었습니다!")

    }

    fun pushStoreLike(storeId:Long): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
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
        var addressId: List<Long>? = addressRepository.findAllBySggNm(addressAndFilter.address)?.map { it.id }
        var output: MutableList<StoreThumbNail> = mutableListOf()
        for (i in addressId?.indices!!) {
            var store = storeRepository.findByAddressId(addressId[i])
            output.add(StoreThumbNail(
                storeId = store.id,
                storeImage = store.storeImage,
                guName = addressRepository.getById(addressId!![i]).sggNm!!,
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
}