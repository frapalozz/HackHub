package plainjava.src.domain.invitation.domain;

import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.user.model.User;

public class InvitationId {
    
    private Team team;
    private User receiver;

    public InvitationId(Team team, User receiver) {
        this.team = team;
        this.receiver = receiver;
    }

    public Team getTeam() {
        return this.team;
    }

    public User getReceiver() {
        return this.receiver;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof InvitationId o)) return false;

        return o.getTeam().equals(team) && o.getReceiver().equals(receiver);
    }
}
