package refresh.onecake.storelike.application

import org.springframework.stereotype.Service
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.member.application.SecurityUtil
import refresh.onecake.review.domain.ReviewRepository
import refresh.onecake.store.domain.StoreRepository
import refresh.onecake.storelike.adapter.dto.LikedStore
import refresh.onecake.storelike.adapter.dto.LikedStoreWithReviewNum
import refresh.onecake.storelike.domain.StoreLike
import refresh.onecake.storelike.domain.StoreLikeRepository

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