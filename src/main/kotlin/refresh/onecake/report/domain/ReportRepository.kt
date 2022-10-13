package refresh.onecake.report.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository: JpaRepository<Report, Long> {
}