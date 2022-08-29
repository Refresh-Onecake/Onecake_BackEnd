package refresh.onecake.dayoff.application

import org.springframework.stereotype.Service
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

    fun setDayOff(dayOffDto: refresh.onecake.adapter.api.dto.DayOffDto): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()
        val dayOff = dayOffRepository.findByStoreIdAndDay(id, dayOffDto.dayOff)
        return if (dayOff != null) {
            dayOffRepository.delete(dayOff)
            DefaultResponseDto(true, "휴무 일정을 취소하였습니다.")
        } else {
            dayOffRepository.save(
                DayOff(
                    storeId = id,
                    day= dayOffDto.dayOff
                )
            )
            DefaultResponseDto(true, "휴무 일정을 등록하였습니다.")
        }

    }

    fun getDayOff(): List<String> {
        val id = SecurityUtil.getCurrentMemberId()
        return dayOffRepository.findAllByStoreIdOrderByDayAsc(id).map { it.day }
    }

}