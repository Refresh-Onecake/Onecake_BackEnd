package refresh.onecake.storelike.service

import org.springframework.stereotype.Service
import refresh.onecake.common.response.DefaultResponseDto
import refresh.onecake.member.service.SecurityUtil
import refresh.onecake.review.repository.ReviewRepository
import refresh.onecake.store.repository.StoreRepository
import refresh.onecake.storelike.dto.LikedStoreWithReviewNum
import refresh.onecake.storelike.entity.StoreLike
import refresh.onecake.storelike.repository.StoreLikeRepository

@Service
class StoreLikeService (
    private val storeLikeRepository: StoreLikeRepository,
    private val storeRepository: StoreRepository,
    private val reviewRepository: ReviewRepository
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

    fun getLikedStoreList(): List<LikedStoreWithReviewNum> {
        val likedStores = storeLikeRepository.getStoresLikedByConsumer(SecurityUtil.getCurrentMemberId())
        val list: MutableList<LikedStoreWithReviewNum> = mutableListOf()
        for (i in likedStores.indices) {
            val store = likedStores[i]
            list.add(
                LikedStoreWithReviewNum(
                    id = store.id,
                    image = store.image,
                    name = store.name,
                    reviewNum = reviewRepository.countByStoreId(store.id)
            ))
        }
        return list
    }
}