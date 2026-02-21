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

        return reportRepository.findAllWhereIsStaff(staffEmail);
    }

    @Override
    public List<SupportRequest> getSupportRequests(String staffEmail) {
        return supportRequestRepository.findAllWhereIsStaff(staffEmail);
    }

    @Override
    public List<HackathonResponse> getAllHackathons() {
        return hackathonRepository
                .findAll()
                .stream()
                .map(hackathonMapper::hackathonToHackathonResponse).
                toList();
    }

    @Override
    public List<AssignedHackathonResponse> getAssignedHackathons(String staffEmail) {

        return hackathonRepository
                .findAllWhereIsStaff(staffEmail)
                .stream()
                .map(h -> hackathonMapper.hackathonToAssignedHackathon(h, staffEmail))
                .toList();
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
}
