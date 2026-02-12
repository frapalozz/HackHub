package unicam.hackhub.application.hackathon;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.hackathon.model.Valuation;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.hackathon.repository.SubmissionRepository;
import unicam.hackhub.domain.hackathon.repository.ValuationRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;

@Service
@Primary
@AllArgsConstructor
public class SubmissionHandlerImpl implements SubmissionHandler {

    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;
    private final SubmissionRepository submissionRepository;
    private final ValuationRepository valuationRepository;

    @Override
    public String addSubmission(String teamName, long hackathonId, Submission submission) {

        Hackathon hackathon = getHackathon(hackathonId);
        Team team = getTeam(teamName);

        hackathon.addSubmission(team, submission);

        hackathonRepository.save(hackathon);

        return "Submission added";
    }

    @Override
    public String updateSubmission(String teamName, long hackathonId, Submission submission) {

        Hackathon hackathon = getHackathon(hackathonId);
        Team team = getTeam(teamName);

        hackathon.updateSubmission(team, submission);

        hackathonRepository.save(hackathon);

        return "Submission updated";
    }

    @Override
    public String valuateSubmission(Long hackathonId, String teamName, int vote, String description) {
        checkValuation(vote, description);

        Hackathon hackathon = getHackathon(hackathonId);

        hackathon.valuateSubmission(teamName, vote, description);

        Valuation valuation = hackathon.getSubmission(teamName).getValuation();

        valuationRepository.save(valuation);

        hackathonRepository.save(hackathon);

        return "Valuation added";
    }

    @Override
    public String editValuation(Long hackathonId, String teamName, int vote, String description) {
        checkValuation(vote, description);

        Hackathon hackathon = getHackathon(hackathonId);

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

    private Team getTeam(String teamName) {
        Team team = teamRepository.findById(teamName).orElse(null);

        if(team == null) {
            throw new IllegalArgumentException("Team not found");
        }

        return team;
    }
}
