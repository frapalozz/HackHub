package code.java.domain.invitation.domain;

import java.time.LocalDate;

public class Invitation {
    
    private InvitationId id;
    private LocalDate date;

    public InvitationId getId() {
        return this.id;
    }
}
