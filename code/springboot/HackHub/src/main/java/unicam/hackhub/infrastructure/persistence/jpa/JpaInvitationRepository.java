package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.invitation.domain.Invitation;
import unicam.hackhub.domain.invitation.domain.InvitationId;
import unicam.hackhub.domain.invitation.repository.InvitationRepository;

import java.util.List;

@Repository
public interface JpaInvitationRepository extends JpaRepository<Invitation, InvitationId>, InvitationRepository {

    @Override
    @Query("SELECT i FROM Invitation i WHERE i.id.receiver.email = :userId")
    List<Invitation> findAll(@Param("userId") String userId);
}
