package unicam.hackhub.presentation.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.domain.hackathon.model.Report;
import unicam.hackhub.presentation.dto.response.ReportResponse;

@Component
public class ReportMapper {

    public ReportResponse reportToReportResponse(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getTeam().getName(),
                report.getHackathon().getId(),
                report.getHackathon().getName(),
                report.getDescription()
        );
    }
}
