package code.java.src.domain.hackathon.model.state;

import code.java.src.domain.hackathon.model.Hackathon;
import code.java.src.domain.hackathon.model.Submission;
import code.java.src.domain.team.model.Team;

public class EndedState implements HackathonState {

    private Hackathon context;

    @Override
    public boolean addTeam(Team team) {
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
