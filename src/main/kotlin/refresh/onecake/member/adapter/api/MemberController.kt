package refresh.onecake.member.adapter.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.member.adapter.api.dto.DefaultResponseDto
import refresh.onecake.member.adapter.api.dto.MemberInformation
import refresh.onecake.member.application.MemberInfo
import refresh.onecake.member.application.S3Uploader


@RestController
@RequestMapping("/api/v1/member")
class MemberController (
    private val memberInfo: MemberInfo
){

//    @Operation(value = "이미지 업로드")
    @PostMapping("/image")
    fun registerStoreImage(@RequestPart("image") image:MultipartFile): String {
        return memberInfo.registerImage(image)
    }

//    @Operation(value = "사용자 정보")
    @GetMapping("")
    fun getMemberInfo(): ResponseEntity<MemberInformation> {
        return ApiResponse.success(HttpStatus.OK, memberInfo.getMemberInfo())
    }
}