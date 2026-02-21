package unicam.hackhub.application.submission;

import unicam.hackhub.application.dto.command.SubmissionCommand;
import unicam.hackhub.domain.hackathon.model.Submission;

import java.util.List;

public interface SubmissionHandler {

    /**
     * Adds a submission for a specific team in a given hackathon.
     *
     * @param command command to execute
     * @return a status message indicating success or failure, with details if an error occurs
     */
    String addSubmission(SubmissionCommand command);

    /**
     * Replaces an existing submission with a new version.
     * Typically used when a team updates their work before the submission deadline.
     *
     * @param command command to execute
     * @return a status message indicating success or failure, with details if an error occurs
     */
    String updateSubmission(SubmissionCommand command);

    /**
     * Assigns or updates a valuation (score and feedback) for a team's submission.
     * This operation is typically performed by a judge.
     *
     * @param hackathonId the unique identifier of the hackathon
     * @param teamName    the name of the team whose submission is being evaluated
     * @param vote        the numeric score assigned to the submission (e.g., 0–10)
     * @param description textual feedback or justification for the score
     * @return a status message indicating success or failure, with details if an error occurs
     */
    String valuateSubmission(String judgeEmail, Long hackathonId, String teamName, int vote, String description);

    /**
     * Modifies an existing valuation for a team's submission.
     * Allows judges to correct or update scores and feedback.
     *
     * @param hackathonId the unique identifier of the hackathon
     * @param teamName    the name of the team whose valuation is being edited
     * @param vote        the new numeric score
     * @param description the revised textual feedback
     * @return a status message indicating success or failure, with details if an error occurs
     */
    String editValuation(String judgeEmail, Long hackathonId, String teamName, int vote, String description);

    /**
     * Retrieves the submission made by a specific team member (or their team) for a given hackathon.
     *
     * @param user        the identifier (email) of the team member
     * @param hackathonId the unique identifier of the hackathon
     * @return the {@link Submission} object associated with the user and hackathon,
     *         or {@code null} if no submission exists
     */
    Submission getSubmissionTeam(String user, Long hackathonId);

    /**
     * Retrieves a specific submission for detailed staff review.
     *
     * @param staffEmail   the email address of the staff member
     * @param hackathonId  the unique identifier of the hackathon
     * @param submissionId the unique identifier of the submission
     * @return the {@link Submission} object with the specified ID
     * @throws IllegalArgumentException if any parameter is null or invalid
     * @throws RuntimeException         if the submission, hackathon, or staff member is not found,
     *                                  or if the staff member is not authorized to view it
     */
    Submission getSubmissionStaff(String staffEmail, Long hackathonId, Long submissionId);

    /**
     * Retrieves all submissions for a given hackathon, intended for staff evaluation.
     *
     * @param staffEmail  the email address of the staff member requesting the submissions
     * @param hackathonId the unique identifier of the hackathon
     * @return a list of {@link Submission} objects for the specified hackathon
     */
    List<Submission> getSubmissions(String staffEmail, Long hackathonId);
}
