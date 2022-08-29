package refresh.onecake.imagelike.application

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.imagelike.domain.ImageLike
import refresh.onecake.imagelike.domain.ImageLikeRepository
import refresh.onecake.member.application.SecurityUtil
import refresh.onecake.menu.domain.ImageRepository
import refresh.onecake.response.adapter.api.ForbiddenException

@Service
class ImageLikeService (
    private val imageLikeRepository: ImageLikeRepository,
    private val imageRepository: ImageRepository
){

    fun postImageLike(menuId: Long, imageId: Long): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val imageLike = imageLikeRepository.findImageLikeByMemberIdAndImageId(id, imageId)
        val image = imageRepository.findByIdOrNull(imageId) ?: throw ForbiddenException("존재하지 않는 imageId입니다.")
        return if (imageLike != null) {
            image.likeNum--
            imageRepository.save(image)
            imageLikeRepository.delete(imageLike)
            DefaultResponseDto(true, "이미지 좋아요를 취소하였습니다.")
        } else {
            image.likeNum++
            imageRepository.save(image)
            imageLikeRepository.save(
                ImageLike(
                    memberId = id,
                    imageId = imageId
                )
            )
            DefaultResponseDto(true, "이미지 좋아요를 추가하였습니다.")
        }
    }
}