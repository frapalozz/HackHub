package unicam.hackhub.application.hackathon;

import unicam.hackhub.domain.hackathon.model.Report;
import unicam.hackhub.domain.support.model.SupportRequest;

import java.util.List;

public interface HackathonViewHandler {


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


}
