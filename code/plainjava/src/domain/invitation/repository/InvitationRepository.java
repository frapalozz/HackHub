package plainjava.src.domain.invitation.repository;

import java.util.List;

import plainjava.src.domain.invitation.domain.Invitation;
import plainjava.src.domain.invitation.domain.InvitationId;

public interface InvitationRepository {
    
    void save(Invitation invitation);
    void saveAll(List<Invitation> invitations);
    Invitation findById(InvitationId id);
}
