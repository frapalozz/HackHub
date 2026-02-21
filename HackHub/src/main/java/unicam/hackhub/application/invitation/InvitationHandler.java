package unicam.hackhub.application.invitation;

import unicam.hackhub.application.dto.response.InvitationResponse;
import unicam.hackhub.domain.team.model.Team;

import java.util.List;

public interface InvitationHandler {

    /**
     * Sends an invitation to a user to join a specific team.
     *
     * @param userId    the identifier of the user to invite
     * @param teamName  the name of the team the user is invited to
     * @return a status message indicating success or failure
     */
    String inviteUser(String userId, String teamName);

    /**
     * Accepts a pending invitation for a user to join a team.
     *
     * @param userEmail the email of the user accepting the invitation
     * @param teamName  the name of the team the invitation belongs to
     * @return a status message indicating success or failure
     */
    String acceptInvitation(String userEmail, String teamName);

    /**
     * Declines a pending invitation for a user to join a team.
     *
     * @param userEmail the email of the user declining the invitation
     * @param teamName  the name of the team the invitation belongs to
     * @return a status message indicating success or failure
     */
    String declineInvitation(String userEmail, String teamName);

    /**
     * Retrieves all invitations (pending, accepted, declined) for a given user.
     *
     * @param userId the identifier of the user whose invitations are requested
     * @return a list of {@link InvitationResponse} DTOs representing the invitations
     */
    List<InvitationResponse> getInvitations(String userId);

    /**
     * Create bulk invitations
     * @param team team associated to the invitation
     * @param invitedUsers invited email list
     */
    void createInvitations(Team team, List<String> invitedUsers);
}
