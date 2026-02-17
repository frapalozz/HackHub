package unicam.hackhub.application.team;

public interface RegisterTeamHandler {

    /**
     * Registers a team for participation in a specific hackathon.
     *
     * @param teamName    the name of the team to register
     * @param hackathonId the unique identifier of the hackathon
     * @return a status message indicating success or failure
     */
    String registerTeam(String teamName, Long hackathonId);
}
