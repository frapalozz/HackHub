package unicam.hackhub.application.hackathon;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.model.Report;
import unicam.hackhub.domain.hackathon.repository.ReportRepository;
import unicam.hackhub.domain.support.model.SupportRequest;
import unicam.hackhub.domain.support.repository.SupportRequestRepository;

import java.util.List;

@Service
@Primary
@AllArgsConstructor
public class HackathonViewHandlerImpl implements HackathonViewHandler {
    private final ReportRepository reportRepository;
    private final SupportRequestRepository supportRequestRepository;

    @Override
    public List<Report> getReports(String staffEmail) {

        return reportRepository.findAllWhereIsStaff(staffEmail);
    }

    @Override
    public List<SupportRequest> getSupportRequests(String staffEmail) {
        return supportRequestRepository.findAllWhereIsStaff(staffEmail);
    }
}
