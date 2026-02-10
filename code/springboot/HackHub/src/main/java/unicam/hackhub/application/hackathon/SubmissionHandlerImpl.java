package unicam.hackhub.application.hackathon;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;

@Service
@Primary
public class SubmissionHandlerImpl implements SubmissionHandler {

    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;

    public SubmissionHandlerImpl(HackathonRepository hackathonRepository, TeamRepository teamRepository) {
        this.hackathonRepository = hackathonRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public String addSubmission(String teamName, long hackathonId, Submission submission) {

        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElse(null);
        Team team = teamRepository.findById(teamName).orElse(null);

        if(hackathon == null || team == null) {
            throw new IllegalArgumentException("Hackathon or Team not found");
        }

        hackathon.addSubmission(team, submission);

        return "Submission added";
    }

    @Override
    public String updateSubmission(String teamName, long hackathonId, Submission submission) {

        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElse(null);
        Team team = teamRepository.findById(teamName).orElse(null);

        if(hackathon == null || team == null) {
            throw new IllegalArgumentException("Hackathon or Team not found");
        }

        hackathon.updateSubmission(team, submission);

        return "Submission updated";
    }

    @Override
    public String valuateSubmission(Long hackathonId, String teamName, int vote, String description) {
        if((vote < 0 || vote > 10) || description.isEmpty()) {
            throw new IllegalArgumentException("Invalid vote");
        }

        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElse(null);

        if(hackathon == null) {
            throw new IllegalArgumentException("Hackathon not found");
        }

        hackathon.valuateSubmission(teamName, vote, description);

        hackathonRepository.save(hackathon);

        return "Valuation added";
    }
}
