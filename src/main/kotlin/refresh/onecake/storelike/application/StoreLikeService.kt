package refresh.onecake.storelike.application

import org.springframework.stereotype.Service
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.member.application.SecurityUtil
import refresh.onecake.storelike.domain.StoreLike
import refresh.onecake.storelike.domain.StoreLikeRepository

@Service
class StoreLikeService (
    private val storeLikeRepository: StoreLikeRepository
){

    fun pushStoreLike(storeId:Long): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val storeLike = storeLikeRepository.findStoreLikeByMemberIdAndStoreId(id, storeId)
        return if (storeLike != null) {
            storeLikeRepository.delete(storeLike)
            DefaultResponseDto(true, "가게 좋아요를 취소하였습니다.")
        } else {
            storeLikeRepository.save(
                StoreLike(
                    memberId = id,
                    storeId = storeId
                )
            )
            DefaultResponseDto(true, "가게 좋아요를 추가하였습니다.")
        }
    }
}