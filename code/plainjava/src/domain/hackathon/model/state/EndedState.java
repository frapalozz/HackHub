package plainjava.src.domain.hackathon.model.state;

import plainjava.src.domain.hackathon.model.Hackathon;
import plainjava.src.domain.hackathon.model.Submission;
import plainjava.src.domain.team.model.Team;

public class EndedState implements HackathonState {

    private Hackathon context;

    public EndedState(Hackathon context) {
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
        return false;
    }

}
