package refresh.onecake.member.application

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.aws.application.S3Uploader
import refresh.onecake.member.adapter.infra.dto.MemberInformation
import refresh.onecake.member.adapter.infra.dto.PhoneNumber
import refresh.onecake.member.adapter.infra.dto.ProfileAndNickname
import refresh.onecake.member.domain.MemberRepository
import refresh.onecake.response.adapter.dto.DefaultResponseDto

@Service
class MemberInfo (
    private val memberRepository: MemberRepository,
    private val s3Uploader: S3Uploader
){
    private val defaultProfileImg: String = "https://onecake-image-bucket.s3.ap-northeast-2.amazonaws.com/472fe03a-20df-4a95-a614-ff0094b27034-Mask%20group.png"

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

    fun showProfile() : ProfileAndNickname{
        val id = SecurityUtil.getCurrentMemberId()
        val member = memberRepository.getById(id)

        return ProfileAndNickname(
            profileImg = member.profileImg ?: defaultProfileImg,
            nickname = member.userName
        )
    }

    fun editProfile(profileAndNickname: ProfileAndNickname) : DefaultResponseDto{
        val id = SecurityUtil.getCurrentMemberId()
        val member = memberRepository.getById(id)
        if (member.profileImg != profileAndNickname.profileImg) {
            member.profileImg = profileAndNickname.profileImg
            memberRepository.save(member)
        }
        if (member.userName != profileAndNickname.nickname) {
            member.userName = profileAndNickname.nickname
            memberRepository.save(member)
        }
        return DefaultResponseDto(true, "프로필 수정이 완료됐습니다.")
    }

    fun showPhoneNumber() : String{
        val id = SecurityUtil.getCurrentMemberId()
        val member = memberRepository.getById(id)
        return member.phoneNumber
    }

    fun editPhoneNumber(phoneNumber: PhoneNumber): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val member = memberRepository.getById(id)
        if (member.phoneNumber != phoneNumber.phoneNumber) {
            member.phoneNumber = phoneNumber.phoneNumber
            memberRepository.save(member)
        }
        return DefaultResponseDto(true, "휴대폰번호 수정이 완료됐습니다.")
    }

}