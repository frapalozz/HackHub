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
    public boolean registerTeam(Team team) {
        throw new IllegalStateException("Hackathon ended");
    }

    @Override
    public boolean addSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Hackathon ended");
    }

    @Override
    public boolean updateSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Hackathon ended");
    }
    
}
