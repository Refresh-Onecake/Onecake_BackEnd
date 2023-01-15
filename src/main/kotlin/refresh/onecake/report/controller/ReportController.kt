package refresh.onecake.report.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import refresh.onecake.report.dto.ReportStore
import refresh.onecake.report.service.ReportService
import refresh.onecake.common.response.ApiResponse
import refresh.onecake.common.response.DefaultResponseDto

@RestController
@RequestMapping("/api/v1/")
class ReportController (
    private val reportService: ReportService
){

    @Operation(summary = "가게 신고하기")
    @PostMapping("report")
    fun pushStoreLike(@RequestBody reportStore: ReportStore): ResponseEntity<DefaultResponseDto> {
        return ApiResponse.success(
            HttpStatus.OK,
            reportService.reportStore(reportStore)
        )
    }
}