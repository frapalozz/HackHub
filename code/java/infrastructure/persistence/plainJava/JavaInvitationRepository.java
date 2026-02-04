package code.java.infrastructure.persistence.plainJava;

import java.util.List;
import java.util.Set;

import code.java.domain.invitation.domain.Invitation;
import code.java.domain.invitation.domain.InvitationId;
import code.java.domain.invitation.repository.InvitationRepository;

public class JavaInvitationRepository implements InvitationRepository {

    private Set<Invitation> invitations;

    @Override
    public void save(Invitation invitation) {
        Invitation invitationPresent = this.findById(invitation.getId());

        if(invitationPresent == null) {
            this.invitations.add(invitation);
        } else {
            this.invitations.remove(invitationPresent);
            this.invitations.add(invitation);
        }
    }

    @Override
    public void saveAll(List<Invitation> invitations) {
        invitations.forEach(i -> this.save(i));
    }

    @Override
    public Invitation findById(InvitationId id) {
        return this.invitations.stream()
            .filter(i -> i.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
}
