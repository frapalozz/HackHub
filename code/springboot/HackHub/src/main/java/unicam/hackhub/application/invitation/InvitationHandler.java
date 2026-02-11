package unicam.hackhub.application.invitation;

import unicam.hackhub.application.invitation.dto.response.InvitationResponse;
import unicam.hackhub.domain.invitation.domain.Invitation;
import unicam.hackhub.domain.invitation.domain.InvitationId;

import java.util.List;

public interface InvitationHandler {

    String inviteUser(String userId, String teamName);
    String acceptInvitation(String userEmail, String teamName);
    String declineInvitation(String userEmail, String teamName);
    List<InvitationResponse> getInvitations(String userId);
}
