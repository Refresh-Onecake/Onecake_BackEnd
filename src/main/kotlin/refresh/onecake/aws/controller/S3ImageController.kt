package refresh.onecake.aws.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import refresh.onecake.aws.service.S3Uploader

//@Api(tags = arrayOf(SwaggerNotes.S3_IMAGE_CONTROLLER_TAG), description = "프로필 사진 업로드")
@RequestMapping("/api/v1/image")
@RestController
class S3ImageController (
    private val s3Uploader: S3Uploader
){
    @Operation(summary = "AWS S3에 프로필 이미지 업로드")
//    @Operation(value = "AWS S3에 프로필 이미지 업로드")
    @PostMapping("/upload")
    fun fileUpload(@RequestParam("image") multipartFile: MultipartFile): String {
        return s3Uploader.upload(multipartFile)
    }
}