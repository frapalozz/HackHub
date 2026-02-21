package unicam.hackhub.application.supportRequest;

import unicam.hackhub.application.dto.response.SupportRequestResponse;
import unicam.hackhub.domain.support.model.SupportRequest;
import unicam.hackhub.domain.utils.TimeRange;

import java.time.LocalDate;
import java.util.List;

public interface CalendarHandler {

    /**
     * Retrieves the available free time slots for a specific mentor on a given date.
     *
     * @param mentorEmail the email address of the mentor whose free slots are requested
     * @param date        the date for which free slots are to be retrieved
     * @return a list of {@link TimeRange} objects representing the free time slots,
     *         or an empty list if none are available
     */
    List<TimeRange> getFreeSlots(String mentorEmail, LocalDate date);

    /**
     * Submits a support request from a team member to a mentor for a specific time slot.
     *
     * @param teamMember   the identifier (email) of the team member requesting support
     * @param hackathonId  the unique identifier of the hackathon for which support is requested
     * @param mentorEmail  the email address of the mentor being requested
     * @param slot         the desired time range for the support session
     * @param date         the date on which the support is requested
     * @return a confirmation message or the generated request ID upon successful submission,
     *         or an error message if the request cannot be processed (e.g., slot unavailable)
     */
    String requestSupport(String teamMember, Long hackathonId, String mentorEmail, TimeRange slot, LocalDate date);

    /**
     * Accepts a pending support request and provides a meeting link for the session.
     *
     * @param mentorEmail the email address of the mentor accepting the request
     * @param requestId   the unique identifier of the support request to accept
     * @param linkCall    the URL or meeting link to be used for the support session
     * @return a success message or the updated request ID upon acceptance,
     *         or an error message if the request cannot be accepted
     */
    String acceptRequest(String mentorEmail, Long requestId, String linkCall);

    /**
     * Declines a pending support request.
     *
     * @param mentorEmail the email address of the mentor declining the request
     * @param requestId   the unique identifier of the support request to decline
     * @return a success message or the updated request ID upon decline,
     *         or an error message if the request cannot be declined
     */
    String declineRequest(String mentorEmail, Long requestId);

    /**
     * Retrieves all support requests assigned to or relevant for a staff member.
     *
     * @param staffEmail the email address of the staff member
     * @return a list of {@link SupportRequest} objects, possibly empty
     */
    List<SupportRequestResponse> getSupportRequests(String staffEmail);
}
