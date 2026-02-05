package plainjava.src.domain.hackathon.model.state;

import plainjava.src.domain.hackathon.model.Hackathon;
import plainjava.src.domain.hackathon.model.Submission;
import plainjava.src.domain.team.model.Team;

import java.time.LocalDate;

public class SubscriptionState implements HackathonState {

    private Hackathon context;

    public SubscriptionState(Hackathon context) {
        this.context = context;
    }

    @Override
    public boolean registerTeam(Team team) {
        if(this.context.getSubscriptionDeadline().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Subscription closed");

        if(this.context.getMaxTeamSize() < team.getMembers().size())
            throw new IllegalArgumentException("Max team size exceeded");

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
