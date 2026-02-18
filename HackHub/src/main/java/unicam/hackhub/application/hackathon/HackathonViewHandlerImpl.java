package unicam.hackhub.application.hackathon;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.application.dto.mapper.HackathonMapper;
import unicam.hackhub.application.dto.response.AssignedHackathonResponse;
import unicam.hackhub.application.dto.response.HackathonResponse;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Report;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.hackathon.repository.ReportRepository;
import unicam.hackhub.domain.hackathon.repository.SubmissionRepository;
import unicam.hackhub.domain.support.model.SupportRequest;
import unicam.hackhub.domain.support.repository.SupportRequestRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@AllArgsConstructor
public class HackathonViewHandlerImpl implements HackathonViewHandler {

    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final SupportRequestRepository supportRequestRepository;
    private final SubmissionRepository submissionRepository;

    private final HackathonMapper hackathonMapper;

    @Override
    public List<HackathonResponse> getPublicHackathons() {
        return hackathonRepository.findPublicHackathons().stream().map(hackathonMapper::hackathonToHackathonResponse).collect(Collectors.toList());
    }

    @Override
    public HackathonResponse getHackathonDetails(Long hackathonId) {
        return hackathonMapper
                .hackathonToHackathonResponse(hackathonRepository
                        .findById(hackathonId)
                        .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"))
                );
    }

    @Override
    public List<Report> getReports(String staffEmail) {
        List<Report> reports = reportRepository.findAllWhereIsStaff(staffEmail);

        if(reports.isEmpty()) {
            throw new IllegalArgumentException("No reports");
        }

        return reports;
    }

    @Override
    public List<SupportRequest> getSupportRequests(String staffEmail) {
        List<SupportRequest> supportRequests = supportRequestRepository.findAllWhereIsStaff(staffEmail);

        if(supportRequests.isEmpty()) {
            throw new IllegalArgumentException("No support requests");
        }

        return supportRequests;
    }

    @Override
    public List<Hackathon> getAllHackathons() {
        List<Hackathon> hackathons = hackathonRepository.findAll();

        if(hackathons.isEmpty()) {
            throw new IllegalArgumentException("No hackathons found");
        }

        return hackathons;
    }

    @Override
    public List<Submission> getSubmissions(String staffEmail, Long hackathonId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if(!hackathon.containsStaff(staffEmail)){
            throw new IllegalArgumentException("staff not in hackathon");
        }

        return hackathon.getSubmissions().values().stream().toList();
    }

    @Override
    public List<AssignedHackathonResponse> getAssignedHackathons(String staffEmail) {

        List<Hackathon> hackathons = hackathonRepository.findAllWhereIsStaff(staffEmail);

        if(hackathons.isEmpty()) {
            throw new IllegalArgumentException("No hackathons found");
        }

        return hackathons.stream()
                .map(h -> hackathonMapper.hackathonToAssignedHackathon(h, staffEmail))
                .toList();
    }

    @Override
    public Submission getSubmissionStaff(String staffEmail, Long submissionId) {
        Hackathon hackathon = hackathonRepository.findHackathonOfSubmission(submissionId);

        if(hackathon == null) {
            throw new IllegalArgumentException("Hackathon not found");
        }

        if(!hackathon.containsStaff(staffEmail)){
            throw new IllegalArgumentException("staff not in hackathon");
        }

        return submissionRepository
                .findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));
    }

    @Override
    public List<HackathonResponse> getParticipatingHackathons(String user) {
        User userRepo = userRepository.findById(user).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Team team = userRepo.getTeam();
        if(team == null) {
            throw new IllegalArgumentException("Team not found");
        }

        List<Hackathon> hackathons = hackathonRepository.findAllByParticipatingTeam(team.getName());

        if(hackathons.isEmpty()) {
            throw new IllegalArgumentException("Team not in a hackathon");
        }

        return hackathons.stream().map(hackathonMapper::hackathonToHackathonResponse).toList();
    }

    @Override
    public Submission getSubmissionTeam(String user, Long hackathonId) {
        User userRepo = userRepository.findById(user).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Team team = userRepo.getTeam();
        if(team == null) {
            throw new IllegalArgumentException("Team not found");
        }

        Hackathon hackathon = hackathonRepository
                .findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        Submission submission = hackathon.getSubmission(team.getName());

        if(submission == null) {
            throw new IllegalArgumentException("Submission not found");
        }

        return submission;
    }
}
