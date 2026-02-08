package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.invitation.domain.Invitation;
import unicam.hackhub.domain.invitation.domain.InvitationId;
import unicam.hackhub.domain.invitation.repository.InvitationRepository;

@Repository
public interface JpaInvitationRepository extends JpaRepository<Invitation, InvitationId>, InvitationRepository {
}
