package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import unicam.hackhub.domain.hackathon.model.Report;
import unicam.hackhub.domain.hackathon.repository.ReportRepository;

public interface JpaReportRepository extends JpaRepository<Report, Long>, ReportRepository {
}
