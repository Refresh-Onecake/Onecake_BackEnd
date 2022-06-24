package refresh.onecake.member.application

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.member.adapter.api.dto.ApplyMenuDto
import refresh.onecake.member.adapter.api.dto.ApplyStoreRequestDto
import refresh.onecake.member.adapter.api.dto.DefaultResponseDto
import refresh.onecake.member.application.util.SecurityUtil
import refresh.onecake.member.domain.common.Question
import refresh.onecake.member.domain.common.QuestionRepository
import refresh.onecake.member.domain.member.MemberRepository
import refresh.onecake.member.domain.seller.*
import java.time.LocalDate

@Service
class SellerService (
    private val storeRepository: StoreRepository,
    private val addressRepository: AddressRepository,
    private val sellerRepository: SellerRepository,
    private val menuRepository: MenuRepository,
    private val questionRepository: QuestionRepository
){

    fun registerStore(applyStoreRequestDto: ApplyStoreRequestDto) : DefaultResponseDto{
        val id = SecurityUtil.getCurrentMemberId()
//        println(memberRepository.findMemberTypeById(id))
        if (storeRepository.existsById(id)) {
            return DefaultResponseDto(false, "이미 입점한 판매자 입니다.")
        }
//        else if (memberRepository.findById(id).) {
//            return DefaultResponseDto(false, "판매자만 입점신청을 할 수 있습니다.")
//        }
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
                storeImage = applyStoreRequestDto.storeImage
            )
            storeRepository.save(store)
            val seller = sellerRepository.getById(id)
            seller.store = store
            sellerRepository.save(seller)
            return DefaultResponseDto(true, "입점 신청을 완료하였습니다.")
        }
    }

    fun registerMenu(applyMenuDto: ApplyMenuDto): DefaultResponseDto {

        val id = SecurityUtil.getCurrentMemberId()
        val menu = Menu(
            store = storeRepository.getById(id),
            menuSize = applyMenuDto.cakeSize,
            image = mutableListOf(applyMenuDto.cakeImage),
            price = applyMenuDto.cakePrice,
            menuDescription = applyMenuDto.cakeDescription,
            taste = applyMenuDto.cakeTaste,
            keyword = Keyword.DISCHARGE
        )
        val savedMenu = menuRepository.save(menu)

        if (applyMenuDto.consumerInput?.isNotEmpty() == true) {
            for (i in 0 until applyMenuDto.consumerInput!!.size){
                var question = Question(
                    menuId = savedMenu.id,
                    question = applyMenuDto.consumerInput!![i],
                    isConsumerInput = true
                )
                questionRepository.save(question)
            }
        }
        if (applyMenuDto.cakeInput?.isNotEmpty() == true) {
            for (i in 0 until applyMenuDto.cakeInput!!.size){
                var question = Question(
                    menuId = savedMenu.id,
                    question = applyMenuDto.cakeInput!![i],
                    isConsumerInput = false
                )
                questionRepository.save(question)
            }
        }

        return DefaultResponseDto(true, "메뉴 등록을 완료하였습니다.")
    }
}