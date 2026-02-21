package unicam.hackhub.application.team;

import java.util.List;

public interface TeamHandler {

    /**
     * Creates a new team with the specified name and creator.
     * <p>
     * The creator becomes the team leader. Optional list of users
     * may be invited to join the team upon creation.
     * </p>
     *
     * @param userId       the identifier of the user creating the team
     * @param teamName     the desired name of the team
     * @param invitedUsers list of user identifiers to invite initially
     * @return a status message indicating success or failure
     */
    String createTeam(String userId, String teamName, List<String> invitedUsers);

    /**
     * Registers a team for participation in a specific hackathon.
     *
     * @param teamName    the name of the team to register
     * @param hackathonId the unique identifier of the hackathon
     * @return a status message indicating success or failure
     */
    String registerTeam(String teamName, Long hackathonId);
}
