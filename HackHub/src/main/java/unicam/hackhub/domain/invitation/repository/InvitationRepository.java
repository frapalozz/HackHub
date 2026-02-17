package unicam.hackhub.domain.invitation.repository;

import unicam.hackhub.domain.invitation.domain.Invitation;
import unicam.hackhub.domain.invitation.domain.InvitationId;
import unicam.hackhub.domain.utils.repository.Delete;
import unicam.hackhub.domain.utils.repository.Find;
import unicam.hackhub.domain.utils.repository.Save;

import java.util.List;

public interface InvitationRepository extends
        Save<Invitation>,
        Find<Invitation, InvitationId>,
        Delete<Invitation> {

    boolean existsById(InvitationId id);
    List<Invitation> findAll(String userId);
}
