package unicam.hackhub.application.submission;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import unicam.hackhub.application.dto.command.SubmissionCommand;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.hackathon.model.Valuation;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.hackathon.repository.ValuationRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;

import java.util.List;

@Service
@Primary
@AllArgsConstructor
public class SubmissionHandlerImpl implements SubmissionHandler {

    private final HackathonRepository hackathonRepository;
    private final ValuationRepository valuationRepository;
    private final UserRepository userRepository;

    @Override
    public String addSubmission(SubmissionCommand command) {

        Hackathon hackathon = getHackathon(command.hackathonId());
        Team team = getUser(command.userEmail()).getTeam();

        hackathon.addSubmission(team, new Submission(command.url()));

        hackathonRepository.save(hackathon);

        return "Submission added";
    }

    @Override
    public String updateSubmission(SubmissionCommand command) {

        Hackathon hackathon = getHackathon(command.hackathonId());
        Team team = getUser(command.userEmail()).getTeam();

        hackathon.updateSubmission(team, new Submission(command.url()));

        hackathonRepository.save(hackathon);

        return "Submission updated";
    }

    @Override
    public String valuateSubmission(String judgeEmail, Long hackathonId, String teamName, int vote, String description) {
        checkValuation(vote, description);

        Hackathon hackathon = getHackathon(hackathonId);

        if(!hackathon.getJudge().getEmail().equals(judgeEmail)) {
            throw new AccessDeniedException("Access denied");
        }

        hackathon.valuateSubmission(teamName, vote, description);

        Valuation valuation = hackathon.getSubmission(teamName).getValuation();

        valuationRepository.save(valuation);

        hackathonRepository.save(hackathon);

        return "Valuation added";
    }

    @Override
    public String editValuation(String judgeEmail, Long hackathonId, String teamName, int vote, String description) {
        checkValuation(vote, description);

        Hackathon hackathon = getHackathon(hackathonId);

        if(!hackathon.getJudge().getEmail().equals(judgeEmail)) {
            throw new AccessDeniedException("Access denied");
        }

        hackathon.updateValuation(teamName, vote, description);

        Valuation valuation = hackathon.getSubmission(teamName).getValuation();

        valuationRepository.save(valuation);

        hackathonRepository.save(hackathon);

        return "Valuation updated";
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

    @Override
    public Submission getSubmissionStaff(String staffEmail, Long hackathonId, Long submissionId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if(!hackathon.containsStaff(staffEmail)){
            throw new IllegalArgumentException("staff not in hackathon");
        }
        if(hackathon.getSubmissions().values().stream().noneMatch(s -> s.getSubmissionId().equals(submissionId))){
            throw new IllegalArgumentException("submission not in hackathon selected");
        }

        return hackathon.getSubmissions().values().stream()
                .filter(s -> s.getSubmissionId().equals(submissionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));
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

    private void checkValuation(int vote, String description) {
        if((vote < 0 || vote > 10) || description.isEmpty()) {
            throw new IllegalArgumentException("Invalid vote");
        }
    }

    private Hackathon getHackathon(Long hackathonId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElse(null);

        if(hackathon == null) {
            throw new IllegalArgumentException("Hackathon not found");
        }

        return hackathon;
    }

    private User getUser(String email) {
        User user = userRepository.findById(email).orElse(null);

        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return user;
    }
}
