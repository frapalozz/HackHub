package plainjava.src.domain.invitation.repository;

import plainjava.src.domain.invitation.domain.Invitation;
import plainjava.src.domain.invitation.domain.InvitationId;
import plainjava.src.domain.utils.repository.Delete;
import plainjava.src.domain.utils.repository.Find;
import plainjava.src.domain.utils.repository.Save;

import java.util.List;

public interface InvitationRepository extends
        Save<Invitation>,
        Find<Invitation, InvitationId>,
        Delete<Invitation> {

    boolean existsById(InvitationId id);
    List<Invitation> findAll(String userId);
}
