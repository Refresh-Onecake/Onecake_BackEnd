package refresh.onecake.report.service

import org.springframework.stereotype.Service
import refresh.onecake.member.service.SecurityUtil
import refresh.onecake.report.dto.ReportStore
import refresh.onecake.report.entity.Report
import refresh.onecake.report.repository.ReportRepository
import refresh.onecake.common.response.DefaultResponseDto

@Service
class ReportService (
    private val reportRepository: ReportRepository
){
    fun reportStore(reportStore: ReportStore): DefaultResponseDto {
        reportRepository.save(Report(
            reporterId = SecurityUtil.getCurrentMemberId(),
            storeName = reportStore.storeName,
            reason = reportStore.reason
        ))
        return DefaultResponseDto(true, "신고가 접수되었습니다.")
    }
}