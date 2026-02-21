package unicam.hackhub.domain.invitation.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.user.model.User;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "invitations")
public class Invitation {

    @EmbeddedId
    private InvitationId id;

    private LocalDate date;

    public Invitation(LocalDate date, Team team, User receiver) {
        this.date = date;
        this.id = new InvitationId(team, receiver);
    }

    public void accept() {
        this.getId().accept();
    }
}
