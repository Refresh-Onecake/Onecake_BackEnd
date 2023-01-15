package refresh.onecake.report.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import refresh.onecake.report.entity.Report

@Repository
interface ReportRepository: JpaRepository<Report, Long> {
}