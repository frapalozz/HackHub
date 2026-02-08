package unicam.hackhub.domain.hackathon.model.state;

import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.team.model.Team;

import java.time.LocalDate;

public class SubscriptionState implements HackathonState {

    private final Hackathon context;

    public SubscriptionState(Hackathon context) {
        this.context = context;
    }

    @Override
    public void registerTeam(Team team) {
        if(this.context.getSubscriptionDeadline().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Subscription closed");

        if(this.context.getMaxTeamSize() < team.getMembers().size())
            throw new IllegalArgumentException("Max team size exceeded");

        if(this.context.hasTeam(team)) {
            throw new IllegalArgumentException("Team already present");
        }
    }

    @Override
    public void addSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Hackathon not yet started");
    }

    @Override
    public void updateSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Hackathon not yet started");
    }

    @Override
    public boolean active() {
        return true;
    }

    @Override
    public void valuateSubmission(String teamName, int vote, String description) {
        throw new IllegalStateException("Hackathon not yet started");
    }

    @Override
    public void declareWinner(Team team) {
        throw new IllegalStateException("Hackathon not yet started");
    }

}
