package plainjava.src.domain.hackathon.model.state;

import plainjava.src.domain.hackathon.model.Hackathon;
import plainjava.src.domain.hackathon.model.Submission;
import plainjava.src.domain.hackathon.model.Valuation;
import plainjava.src.domain.team.model.Team;

public class EvaluationState implements HackathonState {

    private Hackathon context;

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
