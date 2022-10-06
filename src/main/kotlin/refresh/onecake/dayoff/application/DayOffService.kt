package refresh.onecake.dayoff.application

import org.springframework.stereotype.Service
import refresh.onecake.dayoff.adapter.infra.dto.DayOffDto
import refresh.onecake.dayoff.domain.DayOff
import refresh.onecake.dayoff.domain.DayOffRepository
import refresh.onecake.member.application.SecurityUtil
import refresh.onecake.response.adapter.dto.DefaultResponseDto

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