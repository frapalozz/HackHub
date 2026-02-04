package code.java.domain.hackathon.model.state;

import code.java.domain.hackathon.model.Hackathon;
import code.java.domain.hackathon.model.Submission;
import code.java.domain.team.model.Team;

public class ProgressState implements HackathonState {

    private Hackathon context;

    @Override
    public boolean addTeam(Team team) {
        throw new IllegalStateException("Hackathon already started");
    }

    @Override
    public boolean addSubmission(Team team, Submission submission) {
        return true;
    }

    @Override
    public boolean updateSubmission(Team team, Submission submission) {
        if(!this.context.teamHasSubmission(team)) {
            throw new IllegalArgumentException("Team doesn't have a submission");
        }
        return true;
    }
    
}
