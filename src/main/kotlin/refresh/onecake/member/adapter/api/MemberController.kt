package refresh.onecake.member.adapter.api

import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.member.application.S3Uploader

@RestController
@RequestMapping("/api/v1/member")
class MemberController (
//    private val s3Uploader: S3Uploader
){

//    @PostMapping("/password")
//    fun resetPassword():

//    @PostMapping("/profile")
//    @Operation(summary = "AWS S3에 프로필 이미지 업로드")
//    @ApiOperation(value = "AWS S3에 프로필 이미지 업로드")
//    fun fileUpload(@RequestParam("image") multipartFile: MultipartFile): String {
//        return s3Uploader.upload(multipartFile)
//    }
}