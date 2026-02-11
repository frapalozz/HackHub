package unicam.hackhub.domain.hackathon.model.state;

import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.hackathon.model.Valuation;
import unicam.hackhub.domain.team.model.Team;

public class EvaluationState extends AbstractHackathonState {

    public EvaluationState(Hackathon context) {
        super(context);
    }

    @Override
    public boolean active() {
        return true;
    }

    @Override
    public void valuateSubmission(String teamName, int vote, String description) {
        Submission submission = this.hackathon.getSubmission(teamName);

        if (submission == null) {
            throw new RuntimeException("Submission not found");
        }

        if (submission.getValuation() != null) {
            throw new RuntimeException("Submission already valuated");
        }

        submission.setValuation(new Valuation(vote, description));
    }

    @Override
    public void declareWinner(Team team) {
        this.hackathon.setWinner(team);
    }

}
