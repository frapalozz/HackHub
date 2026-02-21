package unicam.hackhub.application.hackathon;

import unicam.hackhub.application.dto.command.CreateHackathonCommand;
import unicam.hackhub.application.dto.response.AssignedHackathonResponse;
import unicam.hackhub.application.dto.response.HackathonResponse;

import java.util.List;

public interface HackathonHandler {

    /**
     * Creates a new hackathon based on the provided request data.
     * <p>
     * The method validates the request (e.g., ensures required fields are present,
     * dates are coherent, and referenced staff exist), then constructs and persists
     * a {@code Hackathon} entity. The returned entity includes its generated
     * identifier and any default state applied during creation.
     * </p>
     *
     * @param request a data transfer object containing all necessary information
     *                for the hackathon, such as name, subscription deadline,
     *                hackathon period, maximum team size, requirements, prize,
     *                organizer, judge, and mentors.
     * @return the fully initialized and persisted {@code Hackathon} entity,
     *         complete with its generated ID and current status
     * @throws IllegalArgumentException if the request is invalid or violates
     *                                  any business rule (e.g., past deadline,
     *                                  non‑existent staff, invalid team size)
     */
    String createHackathon(CreateHackathonCommand request);

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
    String declareWinner(String organizerEmail, Long hackathonId, String teamName);

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
    String addMentors(String organizerEmail, Long hackathonId, List<String> mentorsList);

    /**
     * Retrieves a list of hackathons that are publicly accessible without authentication.
     *
     * @return a list of {@link HackathonResponse} objects representing public hackathons,
     *         or an empty list if none are available
     */
    List<HackathonResponse> getPublicHackathons();

    /**
     * Retrieves all hackathons in the system (typically for administrative purposes).
     *
     * @return a list of all {@link HackathonResponse} objects
     */
    List<HackathonResponse> getAllHackathons();

    /**
     * Retrieves detailed information for a specific hackathon.
     *
     * @param hackathonId the unique identifier of the hackathon
     * @return the {@link HackathonResponse} containing full details of the hackathon
     */
    HackathonResponse getHackathonDetails(Long hackathonId);

    /**
     * Retrieves hackathons that have been assigned to a specific staff member
     * (e.g., as a mentor, judge, or coordinator).
     *
     * @param staffEmail the email address of the staff member
     * @return a list of {@link AssignedHackathonResponse} objects representing assigned hackathons
     */
    List<AssignedHackathonResponse> getAssignedHackathons(String staffEmail);

    /**
     * Retrieves hackathons in which a given user (team member) is participating.
     *
     * @param user the identifier (email) of the team member
     * @return a list of {@link HackathonResponse} objects for hackathons the user is part of
     */
    List<HackathonResponse> getParticipatingHackathons(String user);

}
