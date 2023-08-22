package refresh.onecake.store.service

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import refresh.onecake.address.entity.Address
import refresh.onecake.address.repository.AddressRepository
import refresh.onecake.member.service.SecurityUtil
import refresh.onecake.member.repository.SellerRepository
import refresh.onecake.menu.dto.NeighborhoodStore
import refresh.onecake.menu.dto.StoreMenuListDto
import refresh.onecake.menu.repository.MenuRepository
import refresh.onecake.orderhistory.repository.OrderHistoryRepository
import refresh.onecake.orderhistory.entity.OrderState
import refresh.onecake.common.response.DefaultResponseDto
import refresh.onecake.review.repository.ReviewRepository
import refresh.onecake.store.dto.*
import refresh.onecake.store.entity.Store
import refresh.onecake.store.repository.StoreRepository
import refresh.onecake.storelike.repository.StoreLikeRepository
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

@Service
class StoreService (
    private val storeRepository: StoreRepository,
    private val menuRepository: MenuRepository,
    private val modelMapper: ModelMapper,
    private val addressRepository: AddressRepository,
    private val storeLikeRepository: StoreLikeRepository,
    private val reviewRepository: ReviewRepository,
    private val sellerRepository: SellerRepository,
    private val orderHistoryRepository: OrderHistoryRepository
){

    fun registerStore(applyStoreRequestDto: ApplyStoreRequestDto) : DefaultResponseDto {

        val id = SecurityUtil.getCurrentMemberId()
        if (storeRepository.existsById(id)) {
            return DefaultResponseDto(false, "이미 입점한 판매자 입니다.")
        }
        else {
            val address = Address(
                id = id,
                jibunAddress = applyStoreRequestDto.address?.jibunAddress,
                roadFullAddr = applyStoreRequestDto.address?.roadFullAddr,
                siNm = applyStoreRequestDto.address?.siNm,
                sggNm = applyStoreRequestDto.address?.sggNm,
                emdNm = applyStoreRequestDto.address?.emdNm,
                lnbrMnnm = applyStoreRequestDto.address?.lnbrMnnm,
                addressDetail = applyStoreRequestDto.address?.addressDetail
            )
            addressRepository.save(address)
            val store = Store(
                id = id,
                storeName = applyStoreRequestDto.storeName,
                businessRegistrationNumber = applyStoreRequestDto.businessRegistrationNumber,
                address = address,
                storePhoneNumber = applyStoreRequestDto.storePhoneNumber,
                storeDiscription = applyStoreRequestDto.storeDiscription,
                openTime = applyStoreRequestDto.openTime,
                closeTime = applyStoreRequestDto.closeTime,
                kakaoChannelUrl = applyStoreRequestDto.kakaoChannelUrl,
                storeImage = applyStoreRequestDto.storeImage,
                isActivated = true
            )
            storeRepository.save(store)
            val seller = sellerRepository.getById(id)
            seller.store = store
            sellerRepository.save(seller)
            return DefaultResponseDto(true, "입점 신청을 완료하였습니다.")
        }
    }

    fun storeMainInfo(storeId:Long): StoreMainInfoDto {
        val id = SecurityUtil.getCurrentMemberId()
        val store = storeRepository.getById(storeId)
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
            isLiked = storeLikeRepository.existsByMemberIdAndStoreId(id, storeId),
            chatUrl = store.kakaoChannelUrl
        )
    }

    fun storeMenuList(storeId: Long): List<StoreMenuListDto> {
        return menuRepository.findAllByStoreIdAndIsActivatedOrderByMenuNameAsc(storeId, true)
            .map { modelMapper.map(it, StoreMenuListDto::class.java) }
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

    fun getAllStoreByAddressPopular(): List<StoreThumbNail?> {
        val id = SecurityUtil.getCurrentMemberId()
//        val addressId: List<Long>? = addressRepository.findAllBySggNm(addressAndFilter.address)?.map { it.id }
//        val output: MutableList<StoreThumbNail> = mutableListOf()
        val stores = storeRepository.findAll().map { it ->
            addressRepository.getById(it.id).sggNm?.let { it1 ->
                StoreThumbNail(it.id, it.storeImage,
                        it1, it.storeName,
                        storeLikeRepository.countByStoreId(it.id), reviewRepository.countByStoreId(it.id),
                    storeLikeRepository.existsByMemberIdAndStoreId(id, it.id)
                )
            }
        }
//        output.add(
//                StoreThumbNail(
//                        storeId = store.id,
//                        storeImage = store.storeImage,
//                        guName = addressRepository.getById(addressId[i]).sggNm!!,
//                        storeName = store.storeName,
//                        likedNum = storeLikeRepository.countByStoreId(store.id),
//                        reviewNum = reviewRepository.countByStoreId(store.id),
//                        isLiked = storeLikeRepository.existsByMemberIdAndStoreId(id, store.id)
//                )
//        )
//        if (addressAndFilter.filter == "review") {
//            output.sortByDescending { it.reviewNum }
//        } else {
//            output.sortByDescending { it.likedNum }
//        }
        return stores.sortedByDescending { it?.likedNum }
    }

    fun getAllStoreByAddressReview(): List<StoreThumbNail?> {
        val id = SecurityUtil.getCurrentMemberId()
        val stores = storeRepository.findAll().map { it ->
            addressRepository.getById(it.id).sggNm?.let { it1 ->
                StoreThumbNail(it.id, it.storeImage,
                        it1, it.storeName,
                        storeLikeRepository.countByStoreId(it.id), reviewRepository.countByStoreId(it.id),
                        storeLikeRepository.existsByMemberIdAndStoreId(id, it.id)
                )
            }
        }
        return stores.sortedByDescending { it?.reviewNum }
    }

    /*
        TO DO : N+1 해결
     */
//    fun getNeighborhoodStore(): List<NeighborhoodStore> {
//        val addresses = addressRepository.findAllBySggNm("마포구")
//        val stores: MutableList<Store> = mutableListOf()
//
//        for (i in addresses?.indices!!) {
//            val store = storeRepository.findByAddressIdAndIsActivated(addresses[i].id, true)
//            if(store != null) stores.add(store)
//            if(stores.size >= 10) break
//        }
//        return stores.map { modelMapper.map(it, NeighborhoodStore::class.java) }
//    }


    fun getSellerChatUrl(): String {
        val id = SecurityUtil.getCurrentMemberId()
        return storeRepository.getById(id).kakaoChannelUrl
    }

    fun getSalesData(month: String) : SalesData{
        val id = SecurityUtil.getCurrentMemberId()
        val lastMonth = YearMonth.parse(month, DateTimeFormatter.ofPattern("yyyy-MM")).minusMonths(1).toString()
        return SalesData(
            numOfOrdersThisMonth = orderHistoryRepository.countByPickUpDayStartsWithAndStoreId(month, id),
            numOfSalesThisMonth = orderHistoryRepository.countByPickUpDayStartsWithAndStoreIdAndState(month, id, OrderState.PICKEDUP),
            numOfSalesLastMonth = orderHistoryRepository.countByPickUpDayStartsWithAndStoreIdAndState(lastMonth, id, OrderState.PICKEDUP)
        )
    }

    fun getSalesGraphData(month: String): GraphData{
        val id = SecurityUtil.getCurrentMemberId()
        val formatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("yyyy-MM")
            .toFormatter(Locale.KOREA)
        val monthMinusOne = YearMonth.parse(month, formatter).minusMonths(1).toString()
        val monthMinusTwo = YearMonth.parse(month, formatter).minusMonths(2).toString()
        val monthMinusThree = YearMonth.parse(month, formatter).minusMonths(3).toString()
        val monthMinusFour = YearMonth.parse(month, formatter).minusMonths(4).toString()
        val fiveMonthsOrderHistory = orderHistoryRepository.getSalesGraphData(month, monthMinusOne, monthMinusTwo, monthMinusThree, monthMinusFour,
                                                                                id, OrderState.PICKEDUP.toString())
        println(fiveMonthsOrderHistory)
        return GraphData(
            month = fiveMonthsOrderHistory.count { it.pickUpDay.startsWith(month) },
            monthMinusOne = fiveMonthsOrderHistory.count { it.pickUpDay.startsWith(monthMinusOne) },
            monthMinusTwo = fiveMonthsOrderHistory.count { it.pickUpDay.startsWith(monthMinusTwo) },
            monthMinusThree = fiveMonthsOrderHistory.count { it.pickUpDay.startsWith(monthMinusThree) },
            monthMinusFour = fiveMonthsOrderHistory.count { it.pickUpDay.startsWith(monthMinusFour) }
        )
    }
}