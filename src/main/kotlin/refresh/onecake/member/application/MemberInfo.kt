package refresh.onecake.member.application

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.member.adapter.api.dto.DefaultResponseDto
import refresh.onecake.member.application.util.SecurityUtil
import refresh.onecake.member.domain.member.MemberRepository
import refresh.onecake.member.domain.seller.StoreRepository

@Service
class MemberInfo (
    private val memberRepository: MemberRepository,
    private val storeRepository: StoreRepository,
    private val s3Uploader: S3Uploader
){

    fun registerImage(multipartFile: MultipartFile): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val store = storeRepository.getById(id)
        store.storeImage = s3Uploader.upload(multipartFile)
        return DefaultResponseDto(true, "이미지 등록을 성공하였습니다.")
    }

}