package unicam.hackhub.application.hackathon;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.hackathon.model.Valuation;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.hackathon.repository.ValuationRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;

@Service
@Primary
@AllArgsConstructor
public class SubmissionHandlerImpl implements SubmissionHandler {

    private final HackathonRepository hackathonRepository;
    private final ValuationRepository valuationRepository;
    private final UserRepository userRepository;

    @Override
    public String addSubmission(String teamMember, long hackathonId, Submission submission) {

        Hackathon hackathon = getHackathon(hackathonId);
        Team team = getUser(teamMember).getTeam();

        hackathon.addSubmission(team, submission);

        hackathonRepository.save(hackathon);

        return "Submission added";
    }

    @Override
    public String updateSubmission(String teamName, long hackathonId, Submission submission) {

        Hackathon hackathon = getHackathon(hackathonId);
        Team team = getUser(teamName).getTeam();

        hackathon.updateSubmission(team, submission);

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
