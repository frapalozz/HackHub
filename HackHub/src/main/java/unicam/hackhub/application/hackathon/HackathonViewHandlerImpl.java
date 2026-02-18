package unicam.hackhub.application.hackathon;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Report;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.hackathon.repository.ReportRepository;
import unicam.hackhub.domain.hackathon.repository.SubmissionRepository;
import unicam.hackhub.domain.staff.repository.StaffRepository;
import unicam.hackhub.domain.support.model.SupportRequest;
import unicam.hackhub.domain.support.repository.SupportRequestRepository;
import unicam.hackhub.domain.user.repository.UserRepository;

import java.util.List;

@Service
@Primary
@AllArgsConstructor
public class HackathonViewHandlerImpl implements HackathonViewHandler {

    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final SupportRequestRepository supportRequestRepository;
    private final SubmissionRepository submissionRepository;

    @Override
    public List<Hackathon> getHackathons() {
        return List.of();
    }

    @Override
    public Hackathon getHackathonDetails(Long hackathonId) {
        return null;
    }

    @Override
    public List<Report> getReports(String staffEmail) {
        return List.of();
    }

    @Override
    public List<SupportRequest> getSupportRequests(String staffEmail) {
        return List.of();
    }

    @Override
    public List<Hackathon> getAllHackathons() {
        return List.of();
    }

    @Override
    public List<Submission> getSubmissions(String staffEmail, Long hackathonId) {
        return List.of();
    }

    @Override
    public List<Hackathon> getAssignedHackathons(String staffEmail) {
        return List.of();
    }

    @Override
    public Submission getSubmissionStaff(String staffEmail, Long submissionId) {
        return null;
    }

    @Override
    public List<Hackathon> getParticipatingHackathons(String user) {
        return List.of();
    }

    @Override
    public Submission getSubmissionTeam(String user, Long hackathonId) {
        return null;
    }
}
