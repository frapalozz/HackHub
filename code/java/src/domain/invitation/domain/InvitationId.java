package code.java.src.domain.invitation.domain;

import code.java.src.domain.team.model.Team;
import code.java.src.domain.user.model.User;

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
        if(other == null || !(other instanceof InvitationId)) return false;

        InvitationId o = (InvitationId) other;

        return o.getTeam().equals(team) && o.getReceiver().equals(receiver);
    }
}
