package plainjava.src.domain.invitation.domain;

import java.time.LocalDate;

import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.user.model.User;

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
