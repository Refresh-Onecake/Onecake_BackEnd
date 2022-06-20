package refresh.onecake.member.application

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.util.*


@Component
class S3Uploader (
    private val amazonS3Client: AmazonS3Client
){

    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

//    @Value("\${cloud.aws.s3.dir}")
//    lateinit var dir: String

    fun upload(file: MultipartFile): String {
        val fileName = UUID.randomUUID().toString() + "-" + file.originalFilename
        val objMeta = ObjectMetadata()

        val bytes = IOUtils.toByteArray(file.inputStream)
        objMeta.contentLength = bytes.size.toLong()

        val byteArrayIs = ByteArrayInputStream(bytes)

        amazonS3Client.putObject(PutObjectRequest(bucket,  fileName, byteArrayIs, objMeta)
            .withCannedAcl(CannedAccessControlList.PublicRead))

        return amazonS3Client.getUrl(bucket, fileName).toString()
    }

}