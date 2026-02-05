package code.java.src.domain.hackathon.model.state;

import code.java.src.domain.hackathon.model.Hackathon;
import code.java.src.domain.hackathon.model.Submission;
import code.java.src.domain.team.model.Team;

public class SubscriptionState implements HackathonState {

    private Hackathon context;

    public SubscriptionState(Hackathon context) {
        this.context = context;
    }

    @Override
    public boolean addTeam(Team team) {
        if(this.context.hasTeam(team)) {
            throw new IllegalArgumentException("Team already present");
        }
        return true;
    }

    @Override
    public boolean addSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Hackathon not yet started");
    }

    @Override
    public boolean updateSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Hackathon not yet started");
    }
    
}
