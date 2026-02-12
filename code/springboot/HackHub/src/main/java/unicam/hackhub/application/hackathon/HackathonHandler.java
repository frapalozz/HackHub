package unicam.hackhub.application.hackathon;

import java.util.List;

public interface HackathonHandler {

    /**
     * Declares a team as the winner of a specific hackathon.
     * <p>
     * This operation typically updates the hackathon's winner reference
     * and may trigger subsequent processes such as prize distribution
     * or status changes.
     * </p>
     *
     * @param hackathonId the unique identifier of the hackathon
     * @param teamName    the name of the team to be declared winner;
     *                    the team must be registered in the hackathon
     * @return a status message indicating success or failure,
     *         including details if the operation cannot be completed
     */
    String declareWinner(Long hackathonId, String teamName);

    /**
     * Adds one or more mentors to a hackathon.
     * <p>
     * Mentors are assigned to support participating teams.
     * If a mentor is not already registered in the system,
     * the behaviour depends on the implementation (e.g., auto‑registration
     * may occur, or the operation may fail).
     * </p>
     *
     * @param hackathonId the unique identifier of the hackathon
     * @param mentorsList a list of email addresses identifying the mentors to add;
     *                    each email must correspond to an existing {@code Staff}
     *                    entity, or be resolvable to one
     * @return a status message indicating success or failure,
     *         with details about which mentors were added or why the operation failed
     */
    String addMentors(Long hackathonId, List<String> mentorsList);
}
