package refresh.onecake.menu.application

import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import refresh.onecake.member.application.SecurityUtil
import refresh.onecake.menu.adapter.infra.dto.*
import refresh.onecake.menu.domain.*
import refresh.onecake.question.domain.Question
import refresh.onecake.question.domain.QuestionRepository
import refresh.onecake.response.adapter.api.ForbiddenException
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.store.domain.Store
import refresh.onecake.store.domain.StoreRepository

@Service
class MenuService (
    private val imageRepository: ImageRepository,
    private val menuRepository: MenuRepository,
    private val storeRepository: StoreRepository,
    private val modelMapper: ModelMapper,
    private val questionRepository: QuestionRepository
){

    //TODO : menu에 image 추가
    fun registerMenu(applyMenuDto: ApplyMenuDto): DefaultResponseDto {

        val id = SecurityUtil.getCurrentMemberId()

        if (menuRepository.existsByMenuSizeAndStoreIdAndIsActivated(applyMenuDto.cakeSize, id, true)) {
            return DefaultResponseDto(false, "해당 메뉴 사이즈가 이미 존재합니다.")
        }

        val menu = Menu(
            storeId = id,
            menuName = applyMenuDto.cakeSize + " 커스텀 케이크",
            menuSize = applyMenuDto.cakeSize,
            image = applyMenuDto.cakeImage,
            price = applyMenuDto.cakePrice,
            menuDescription = applyMenuDto.cakeDescription,
            taste = applyMenuDto.cakeTaste,
            isActivated = true
        )
        val savedMenu = menuRepository.save(menu)

        val image = Image(
            menuId = savedMenu.id,
            image = applyMenuDto.cakeImage,
            keyword = null,
            likeNum = 0,
            isActivated = true
        )
        imageRepository.save(image)

        if (applyMenuDto.consumerInput.isNotEmpty()) {
            for (i in 0 until applyMenuDto.consumerInput.size){
                val question = Question(
                    menuId = savedMenu.id,
                    question = applyMenuDto.consumerInput[i],
                    isConsumerInput = true,
                    isActivated = true
                )
                questionRepository.save(question)
            }
        }
        if (applyMenuDto.cakeInput.isNotEmpty()) {
            for (i in 0 until applyMenuDto.cakeInput.size){
                val question = Question(
                    menuId = savedMenu.id,
                    question = applyMenuDto.cakeInput[i],
                    isConsumerInput = false,
                    isActivated = true
                )
                questionRepository.save(question)
            }
        }

        return DefaultResponseDto(true, "메뉴 등록을 완료하였습니다.")
    }

    fun getMenus() : List<StoreMenuListAndIdDto>{
        val id = SecurityUtil.getCurrentMemberId()
        return menuRepository.findAllByStoreIdAndIsActivatedOrderByMenuNameAsc(id, true).map{ modelMapper.map(it, StoreMenuListAndIdDto::class.java) }
    }

    fun editMenu(menuId: Long, applyMenuDto: ApplyMenuDto): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val menu = menuRepository.findByIdOrNull(menuId) ?: throw ForbiddenException("해당 메뉴 id는 존재하지 않습니다.")
        if(menu.storeId != id) throw ForbiddenException("해당 메뉴의 판매자가 아닙니다.")
        if (!menu.isActivated) throw ForbiddenException("해당 메뉴는 삭제된 상태입니다.")

        val isExistingMenuId = menuRepository.findByMenuSizeAndStoreIdAndIsActivated(applyMenuDto.cakeSize, id, true)?.id
        if (isExistingMenuId != null && menuId != isExistingMenuId) {
            return DefaultResponseDto(false, "해당 메뉴 사이즈가 이미 존재합니다.")
        }

        menu.id = menuId
        menu.storeId = id
        menu.menuName = applyMenuDto.cakeSize + " 커스텀 케이크"
        menu.menuSize = applyMenuDto.cakeSize
        menu.price = applyMenuDto.cakePrice
        menu.menuDescription = applyMenuDto.cakeDescription
        menu.taste = applyMenuDto.cakeTaste
        menu.image = applyMenuDto.cakeImage

        menuRepository.save(menu)

        val consumerInputQuestions = applyMenuDto.consumerInput
        val cakeInputQuestions = applyMenuDto.cakeInput
        val questions = questionRepository.findAllByMenuIdAndIsActivated(menuId, true)

        questions.forEach { it.isActivated = false }
        questionRepository.saveAll(questions)

        for (i in consumerInputQuestions.indices) {
            questionRepository.save(
                Question(
                    menuId = menuId,
                    question = consumerInputQuestions[i],
                    isConsumerInput = true,
                    isActivated = true
                )
            ) }

        for (i in cakeInputQuestions.indices) {
            questionRepository.save(
                Question(
                    menuId = menuId,
                    question = cakeInputQuestions[i],
                    isConsumerInput = false,
                    isActivated = true
                )
            ) }

        return DefaultResponseDto(true, "메뉴 수정을 성공했습니다.")

    }

    fun deleteMenu(menuId: Long): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()

        val menu = menuRepository.findByIdOrNull(menuId) ?: throw ForbiddenException("존재하지 않는 menuId입니다.")

        if(menu.storeId != id) throw ForbiddenException("해당 메뉴의 판매자가 아닙니다.")

        menu.isActivated = false
        menuRepository.save(menu)

        val images = imageRepository.findAllByMenuId(menuId)
        for (i in images.indices) {
            images[i].isActivated = false
            imageRepository.save(images[i])
        }

        return DefaultResponseDto(true, "메뉴 삭제를 성공했습니다.")
    }

    fun getCakesSize(storeId: Long): StoreNameAndCakeSizesDto {
        return StoreNameAndCakeSizesDto(
            storeName = storeRepository.findStoreById(storeId).storeName,
            sizes = menuRepository.findAllIdAndMenuSizeByStoreIdAndIsActivatedOrderByMenuSizeAsc(storeId, true)
                ?.map { modelMapper.map(it, MenuIdAndSizeDto::class.java) }
        )
    }

    fun getWeeksHottestCake(): List<HomeImages> {
        val images = imageRepository.findTop10ByIsActivatedOrderByLikeNumDesc(true)

        val menus: MutableList<Menu> = mutableListOf()
        for (i in images.indices) {
            menus.add(menuRepository.findMenuById(images[i].menuId))
        }

        val stores: MutableList<Store> = mutableListOf()
        for (i in images.indices) {
            stores.add(storeRepository.findStoreById(menus[i].storeId))
        }

        val outputs: MutableList<HomeImages> = mutableListOf()
        for (i in images.indices) {
            outputs.add(
                HomeImages(
                    image = images[i].image,
                    storeId = stores[i].id,
                    menuId = menus[i].id,
                    imageId = images[i].id
                )
            )
        }
        return outputs
    }

    fun getStoredMenuForm(menuId: Long): StoredMenuForm {
        val menu = menuRepository.findMenuById(menuId)
        val question = questionRepository.findAllByMenuIdAndIsActivated(menuId, true)
        return StoredMenuForm(
            cakeSize = menu.menuSize,
            image = menu.image,
            price = menu.price,
            menuDescription = menu.menuDescription,
            taste = menu.taste,
            consumerInput = question.filter { it.isConsumerInput && it.isActivated }.map { it.question },
            cakeInput = question.filter { !it.isConsumerInput && it.isActivated }.map { it.question }

        )
    }

}