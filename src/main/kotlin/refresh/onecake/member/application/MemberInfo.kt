package refresh.onecake.member.application

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.aws.application.S3Uploader
import refresh.onecake.member.adapter.infra.dto.MemberInformation
import refresh.onecake.member.domain.MemberRepository

@Service
class MemberInfo (
    private val memberRepository: MemberRepository,
    private val s3Uploader: S3Uploader
){

    fun registerImage(multipartFile: MultipartFile): String {
        return s3Uploader.upload(multipartFile)
    }

    fun getMemberInfo(): MemberInformation {
        val id = SecurityUtil.getCurrentMemberId()
        val member = memberRepository.getById(id)
        return MemberInformation(
            userName = member.userName,
            userId = member.userId,
            phoneNumber = member.phoneNumber
        )
    }

}