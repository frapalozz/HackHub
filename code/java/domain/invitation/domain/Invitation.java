package code.java.domain.invitation.domain;

import java.time.LocalDate;

import code.java.domain.team.model.Team;
import code.java.domain.user.model.User;

public class Invitation {
    
    private InvitationId id;
    private LocalDate date;

    public Invitation(LocalDate date, Team team, User receiver) {
        this.date = date;
        this.id = new InvitationId(team, receiver);
    }

    public InvitationId getId() {
        return this.id;
    }
}
