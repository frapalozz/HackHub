package unicam.hackhub.domain.hackathon.model.state;

import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.team.model.Team;

import java.time.LocalDate;

public class SubscriptionState extends AbstractHackathonState {

    public SubscriptionState(Hackathon context) {
        super(context);
    }

    @Override
    public void registerTeam(Team team) {
        if(this.hackathon.getSubscriptionDeadline().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Subscription closed");

        if(this.hackathon.getMaxTeamSize() < team.getMembers().size())
            throw new IllegalArgumentException("Max team size exceeded");

        if(this.hackathon.hasTeam(team)) {
            throw new IllegalArgumentException("Team already present");
        }
    }
}
