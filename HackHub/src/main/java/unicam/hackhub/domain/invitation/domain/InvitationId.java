package unicam.hackhub.domain.invitation.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.user.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class InvitationId {

    @ManyToOne
    @JoinColumn(name = "team_name", referencedColumnName = "name")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "receiver_email", referencedColumnName = "email")
    private User receiver;

    public void accept() {
        if(receiver.hasTeam()) {
            throw new IllegalArgumentException("Can't accept, user already has team");
        }
        this.team.addMember(receiver);
        this.receiver.setTeam(team);
    }
}
