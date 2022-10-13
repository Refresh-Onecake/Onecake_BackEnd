package refresh.onecake.report.application

import org.springframework.stereotype.Service
import refresh.onecake.member.application.SecurityUtil
import refresh.onecake.report.adapter.dto.ReportStore
import refresh.onecake.report.domain.Report
import refresh.onecake.report.domain.ReportRepository
import refresh.onecake.response.adapter.dto.DefaultResponseDto

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