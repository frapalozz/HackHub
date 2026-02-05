package code.java.src.domain.invitation.repository;

import java.util.List;

import code.java.src.domain.invitation.domain.Invitation;
import code.java.src.domain.invitation.domain.InvitationId;

public interface InvitationRepository {
    
    void save(Invitation invitation);
    void saveAll(List<Invitation> invitations);
    Invitation findById(InvitationId id);
}
