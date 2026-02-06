package plainjava.src.infrastructure.persistence.plainJava;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import plainjava.src.domain.invitation.domain.Invitation;
import plainjava.src.domain.invitation.domain.InvitationId;
import plainjava.src.domain.invitation.repository.InvitationRepository;

public class JavaInvitationRepository implements InvitationRepository {

    private final Set<Invitation> invitations = new HashSet<>();

    @Override
    public Invitation save(Invitation invitation) {
        Invitation invitationPresent = this.findById(invitation.getId());

        if(invitationPresent == null) {
            this.invitations.add(invitation);
        } else {
            this.invitations.remove(invitationPresent);
            this.invitations.add(invitation);
        }

        return invitation;
    }

    @Override
    public void saveAll(List<Invitation> invitations) {
        invitations.forEach(this::save);
    }

    @Override
    public Invitation findById(InvitationId id) {
        return this.invitations.stream()
            .filter(i -> i.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Invitation> findAll(List<InvitationId> ids) {
        return invitations.stream()
                .filter(i -> ids.contains(i.getId()))
                .toList();
    }

    @Override
    public boolean existsById(InvitationId id) {
        return invitations.stream().anyMatch(i -> i.getId().equals(id));
    }
}
