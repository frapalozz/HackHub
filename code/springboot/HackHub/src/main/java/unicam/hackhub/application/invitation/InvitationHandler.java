package unicam.hackhub.application.invitation;

import unicam.hackhub.domain.invitation.domain.Invitation;
import unicam.hackhub.domain.invitation.domain.InvitationId;

import java.util.List;

public interface InvitationHandler {

    String inviteUser(String userId, String teamName);
    String acceptInvitation(InvitationId invitationId);
    String declineInvitation(InvitationId invitationId);
    List<Invitation> getInvitations(String userId);
}
