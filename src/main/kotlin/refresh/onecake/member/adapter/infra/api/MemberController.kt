package refresh.onecake.member.adapter.infra.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.member.adapter.infra.dto.MemberInformation
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




    @Operation(summary = "판매자 회원탈퇴하기")
    @PostMapping("/seller/resign")
    fun resign(): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(HttpStatus.OK, memberService.resign())
    }

}