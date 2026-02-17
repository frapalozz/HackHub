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
    public void updateValuation(String teamName, int vote, String description) {
        Submission submission = this.hackathon.getSubmission(teamName);

        if (submission == null) {
            throw new RuntimeException("Submission not found");
        }

        if (submission.getValuation() == null) {
            throw new RuntimeException("Submission not valuated");
        }

        submission.getValuation().setVote(vote);
        submission.getValuation().setDescription(description);
    }

    @Override
    public void declareWinner(String teamName) {

        boolean anyMissingValuation = hackathon.getSubmissions().values().stream()
                .anyMatch(s -> s.getValuation() == null);
        
        if (anyMissingValuation) {
            throw new IllegalStateException("Missing valuation");
        }

        Team team = hackathon.getTeams().stream()
                .filter(t -> t.getName().equals(teamName))
                .findFirst().orElse(null);

        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }

        this.hackathon.setWinner(team);
    }

    @Override
    public void toNextState() {
        hackathon.changeState(hackathon.getStatus().getNextState());
    }

}
