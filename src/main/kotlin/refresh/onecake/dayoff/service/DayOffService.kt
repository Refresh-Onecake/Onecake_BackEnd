package refresh.onecake.dayoff.service

import org.springframework.stereotype.Service
import refresh.onecake.dayoff.dto.DayOffDto
import refresh.onecake.dayoff.entity.DayOff
import refresh.onecake.dayoff.repository.DayOffRepository
import refresh.onecake.member.service.SecurityUtil
import refresh.onecake.common.response.DefaultResponseDto

@Service
class DayOffService (
    private val dayOffRepository: DayOffRepository
){

    fun getStoreDayOffs(storeId: Long) : List<String>? {
        return dayOffRepository.findAllByStoreIdOrderByDayAsc(storeId).map { it.day }
    }

    fun setDayOff(dayOffDto: DayOffDto): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()

        val specificMonth = dayOffDto.dayOff[0].subSequence(0, 8).toString()
        println(specificMonth)
        dayOffRepository.deleteAllByDayStartsWithAndStoreId(specificMonth, id)

        val entities: MutableList<DayOff> = mutableListOf()
        for (i in dayOffDto.dayOff.indices) {
            entities.add(DayOff(
                storeId = id,
                day = dayOffDto.dayOff[i]
            ))
        }
        dayOffRepository.saveAll(entities)

        return DefaultResponseDto(true, "휴무 일정을 등록하였습니다.")

    }

    fun getDayOff(): List<String> {
        val id = SecurityUtil.getCurrentMemberId()
        return dayOffRepository.findAllByStoreIdOrderByDayAsc(id).map { it.day }
    }

}