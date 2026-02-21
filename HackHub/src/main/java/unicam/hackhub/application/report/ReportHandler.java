package unicam.hackhub.application.report;

import unicam.hackhub.application.dto.response.ReportResponse;
import unicam.hackhub.domain.hackathon.model.Report;

import java.util.List;

public interface ReportHandler {

    /**
     * Submits a report against a specific team in a hackathon.
     *
     * @param teamName    the name of the team being reported
     * @param hackathonId the unique identifier of the hackathon
     * @param description detailed reason for the report
     * @return a status message indicating success or failure
     */
    String report(String mentorEmail, String teamName, Long hackathonId, String description);

    /**
     * Retrieves all reports associated with a staff member (e.g., assigned for review).
     *
     * @param staffEmail the email address of the staff member
     * @return a list of {@link Report} objects, possibly empty
     */
    List<ReportResponse> getReports(String staffEmail);
}
