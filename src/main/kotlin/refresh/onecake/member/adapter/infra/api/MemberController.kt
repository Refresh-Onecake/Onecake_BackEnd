package refresh.onecake.member.adapter.infra.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.member.adapter.infra.dto.MemberInformation
import refresh.onecake.member.adapter.infra.dto.PhoneNumber
import refresh.onecake.member.adapter.infra.dto.ProfileAndNickname
import refresh.onecake.response.adapter.api.ApiResponse
import refresh.onecake.member.application.MemberInfo
import refresh.onecake.member.application.MemberService


@RestController
@RequestMapping("/api/v1")
class MemberController (
    private val memberInfo: MemberInfo,
    private val memberService: MemberService
){

    @Operation(summary = "이미지 업로드")
    @PostMapping("/member/image")
    fun registerStoreImage(@RequestPart("image") image:MultipartFile): String {
        return memberInfo.registerImage(image)
    }

    @Operation(summary = "사용자 정보")
    @GetMapping("/member")
    fun getMemberInfo(): ResponseEntity<MemberInformation> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.getMemberInfo())
    }

    @Operation(summary = "프로필 수정전 프로필,닉네임 넘겨받는 api")
    @GetMapping("/member/profile")
    fun showProfile(): ResponseEntity<ProfileAndNickname> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.showProfile())
    }

    @Operation(summary = "프로필 수정하기")
    @PutMapping("/member/profile")
    fun editProfile(@RequestBody profileAndNickname: ProfileAndNickname): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.editProfile(profileAndNickname))
    }

    @Operation(summary = "휴대폰번호 수정전 휴대폰번호 넘겨받는 api")
    @GetMapping("/member/phoneNumber")
    fun showPhoneNumber(): ResponseEntity<String> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.showPhoneNumber())
    }

    @Operation(summary = "휴대폰번호 수정하기")
    @PutMapping("/member/phoneNumber")
    fun editPhoneNumber(@RequestBody phoneNumber: PhoneNumber): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.editPhoneNumber(phoneNumber))
    }



    @Operation(summary = "판매자 회원탈퇴하기")
    @PostMapping("/seller/resign")
    fun resign(): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, memberService.resign())
    }


}