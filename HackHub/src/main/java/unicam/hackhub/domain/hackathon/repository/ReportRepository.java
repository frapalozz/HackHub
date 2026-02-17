package unicam.hackhub.domain.hackathon.repository;

import unicam.hackhub.domain.hackathon.model.Report;
import unicam.hackhub.domain.utils.repository.Find;
import unicam.hackhub.domain.utils.repository.Save;

public interface ReportRepository extends Find<Report, Long>, Save<Report> {
}
