package refresh.onecake.menu.application

import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import refresh.onecake.imagelike.domain.ImageLikeRepository
import refresh.onecake.member.application.SecurityUtil
import refresh.onecake.menu.adapter.infra.dto.*
import refresh.onecake.menu.domain.Image
import refresh.onecake.menu.domain.ImageRepository
import refresh.onecake.menu.domain.Keyword
import refresh.onecake.menu.domain.MenuRepository
import refresh.onecake.response.adapter.api.ForbiddenException
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.store.domain.StoreRepository

@Service
class ImageService (
    private val imageRepository: ImageRepository,
    private val storeRepository: StoreRepository,
    private val menuRepository: MenuRepository,
    private val imageLikeRepository: ImageLikeRepository,
    private val modelMapper: ModelMapper
){

    fun getImagesOfSpecificMenu(menuId: Long): MenuImageSetting {
        val id = SecurityUtil.getCurrentMemberId()
        val store = storeRepository.findStoreById(id)
        val menu = menuRepository.findByIdOrNull(menuId) ?: throw ForbiddenException("접근할 수 없는 menu Id 입니다.")
        val images = imageRepository.findAllByMenuIdAndIsActivatedAndKeywordIsNotNull(menuId, true).map{ modelMapper.map(it, ImageIdAndImage::class.java) }
        return MenuImageSetting(
            storeName = store.storeName,
            menuTaste = menu.taste,
            images = images
        )
    }

    fun postImage(menuId: Long, imageAndKeyword: ImageAndKeyword): DefaultResponseDto {
        imageRepository.save(
            Image(
                menuId = menuId,
                image = imageAndKeyword.image,
                keyword = imageAndKeyword.keyword,
                likeNum = 0,
                isActivated = true
            )
        )
        return DefaultResponseDto(true, "이미지 등록을 성공하였습니다.")
    }

    fun getImageDetail(menuId: Long, imageId: Long): ImageDetail {
        val id = SecurityUtil.getCurrentMemberId()
        val store = storeRepository.findStoreById(id)
        val menu = menuRepository.findMenuById(menuId)
        val image = imageRepository.findImageById(imageId)

        return if (image.keyword == null) {
            ImageDetail(
                storeName = store.storeName,
                keyWord = menu.menuName,
                imageDescription = menu.menuDescription,
                image = image.image,
                isLiked = imageLikeRepository.existsByMemberIdAndImageId(id, imageId)
            )
        } else {
            ImageDetail(
                storeName = store.storeName,
                keyWord = image.keyword.toString(),
                imageDescription = image.keyword.toString() + "기념 케이크에요.",
                image = image.image,
                isLiked = imageLikeRepository.existsByMemberIdAndImageId(id, imageId)
            )
        }
    }

    fun getAllImagesOfSpecificMenu(storeId: Long, menuId: Long): MenuDescAndImages {
        val images = imageRepository.findAllByMenuId(menuId)
        val store = storeRepository.findStoreById(storeId)
        val menu = menuRepository.findMenuById(menuId)

        return MenuDescAndImages(
            storeName = store.storeName,
            menuName = menu.menuName,
            menuDescription = menu.menuDescription,
            allImages = images.map { modelMapper.map(it, MenuIdAndImage::class.java) },
            birthdayImages = images.filter { it.keyword == Keyword.BIRTHDAY }
                .map { modelMapper.map(it, MenuIdAndImage::class.java) },
            monthlyEventImages = images.filter { it.keyword == Keyword.MONTHLY_EVENT }
                .map { modelMapper.map(it, MenuIdAndImage::class.java) },
            anniversaryImages = images.filter { it.keyword == Keyword.ANNIVERSARY }
                .map { modelMapper.map(it, MenuIdAndImage::class.java) },
            employmentImages = images.filter { it.keyword == Keyword.EMPLOYMENT }
                .map { modelMapper.map(it, MenuIdAndImage::class.java) },
            marriageImages = images.filter { it.keyword == Keyword.MARRIAGE }
                .map { modelMapper.map(it, MenuIdAndImage::class.java) },
            dischargeImages = images.filter { it.keyword == Keyword.DISCHARGE }
                .map { modelMapper.map(it, MenuIdAndImage::class.java) }
        )
    }

    fun deleteImage(imageId: Long): DefaultResponseDto {
        val image = imageRepository.findByIdOrNull(imageId) ?: throw ForbiddenException("imageId에 해당하는 이미지가 존재하지 않습니다.")
        image.isActivated = false
        imageRepository.save(image)
        return DefaultResponseDto(true, "이미지를 삭제하였습니다.")
    }

    fun getKeywordCakes(): List<KeywordImages> {

        var keywordImages = mutableListOf<KeywordImages>()
        getKeyWordImage(Keyword.BIRTHDAY)?.let { keywordImages.add(it) }
        getKeyWordImage(Keyword.MONTHLY_EVENT)?.let { keywordImages.add(it) }
        getKeyWordImage(Keyword.ANNIVERSARY)?.let { keywordImages.add(it) }
        getKeyWordImage(Keyword.EMPLOYMENT)?.let { keywordImages.add(it) }
        getKeyWordImage(Keyword.MARRIAGE)?.let { keywordImages.add(it) }
        getKeyWordImage(Keyword.DISCHARGE)?.let { keywordImages.add(it) }
        getKeyWordImage(Keyword.ETC)?.let { keywordImages.add(it) }

        return keywordImages
    }

    fun getKeyWordImage(keyword: Keyword): KeywordImages? {
        val keyWordImage = imageRepository.findFirstByKeywordAndIsActivatedOrderByCreatedAtDesc(keyword, true)
        return if (keyWordImage != null) {
            val menuAndStore = menuRepository.findMenuWithStore(keyWordImage.menuId)
            KeywordImages(
                image = keyWordImage.image,
                storeId = menuAndStore.storeId,
                menuId = menuAndStore.menuId,
                imageId = keyWordImage.id,
                keyword = keyword
            )
        } else null
    }
}