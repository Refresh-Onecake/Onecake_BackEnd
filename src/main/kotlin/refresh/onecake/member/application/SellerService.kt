package refresh.onecake.member.application

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.member.adapter.api.dto.ApplyStoreRequestDto
import refresh.onecake.member.adapter.api.dto.DefaultResponseDto
import refresh.onecake.member.application.util.SecurityUtil
import refresh.onecake.member.domain.member.MemberRepository
import refresh.onecake.member.domain.seller.*

@Service
class SellerService (
    private val memberRepository: MemberRepository,
    private val storeRepository: StoreRepository,
    private val addressRepository: AddressRepository,
    private val sellerRepository: SellerRepository,
    private val s3Uploader: S3Uploader
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
                storeImage = null
            )
            storeRepository.save(store)
            val seller = sellerRepository.getById(id)
            seller.store = store
            sellerRepository.save(seller)
            return DefaultResponseDto(true, "입점 신청을 완료하였습니다.")
        }
    }

    fun registerStoreImage(multipartFile: MultipartFile): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val store = storeRepository.getById(id)
        store.storeImage = s3Uploader.upload(multipartFile)
        return DefaultResponseDto(true, "가게의 이미지 등록을 성공하였습니다.")
    }
}