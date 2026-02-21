package unicam.hackhub.application.hackathon;

import unicam.hackhub.application.dto.response.AssignedHackathonResponse;
import unicam.hackhub.application.dto.response.HackathonResponse;
import unicam.hackhub.domain.hackathon.model.Report;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.support.model.SupportRequest;

import java.util.List;

public interface HackathonViewHandler {

    /**
     * Retrieves a list of hackathons that are publicly accessible without authentication.
     *
     * @return a list of {@link HackathonResponse} objects representing public hackathons,
     *         or an empty list if none are available
     */
    List<HackathonResponse> getPublicHackathons();

    /**
     * Retrieves detailed information for a specific hackathon.
     *
     * @param hackathonId the unique identifier of the hackathon
     * @return the {@link HackathonResponse} containing full details of the hackathon
     */
    HackathonResponse getHackathonDetails(Long hackathonId);

    /**
     * Retrieves all reports associated with a staff member (e.g., assigned for review).
     *
     * @param staffEmail the email address of the staff member
     * @return a list of {@link Report} objects, possibly empty
     */
    List<Report> getReports(String staffEmail);

    /**
     * Retrieves all support requests assigned to or relevant for a staff member.
     *
     * @param staffEmail the email address of the staff member
     * @return a list of {@link SupportRequest} objects, possibly empty
     */
    List<SupportRequest> getSupportRequests(String staffEmail);

    /**
     * Retrieves all hackathons in the system (typically for administrative purposes).
     *
     * @return a list of all {@link HackathonResponse} objects
     */
    List<HackathonResponse> getAllHackathons();

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
