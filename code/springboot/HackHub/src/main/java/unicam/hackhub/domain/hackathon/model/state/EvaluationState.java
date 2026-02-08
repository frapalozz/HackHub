package unicam.hackhub.domain.hackathon.model.state;

import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.hackathon.model.Valuation;
import unicam.hackhub.domain.team.model.Team;

public class EvaluationState implements HackathonState {

    private final Hackathon context;

    public EvaluationState(Hackathon context) {
        this.context = context;
    }

    @Override
    public void registerTeam(Team team) {
        throw new IllegalStateException("Hackathon ended");
    }

    @Override
    public void addSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Hackathon ended");
    }

    @Override
    public void updateSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Hackathon ended");
    }

    @Override
    public boolean active() {
        return true;
    }

    @Override
    public void valuateSubmission(String teamName, int vote, String description) {
        this.context.getSubmission(teamName).setValuation(new Valuation(vote, description));
    }

    @Override
    public void declareWinner(Team team) {
        this.context.setWinner(team);
    }

}
