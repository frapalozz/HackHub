package unicam.hackhub.application.hackathon;

import unicam.hackhub.domain.hackathon.model.Submission;

public interface SubmissionHandler {

    /**
     * Adds a submission for a specific team in a given hackathon.
     *
     * @param teamName     the name of the team submitting the work; must exist and belong to the hackathon
     * @param hackathonId  the unique identifier of the hackathon
     * @param submission   the submission entity containing details such as content, files, and metadata
     * @return a status message indicating success or failure, with details if an error occurs
     */
    String addSubmission(String teamName, long hackathonId, Submission submission);

    /**
     * Replaces an existing submission with a new version.
     * Typically used when a team updates their work before the submission deadline.
     *
     * @param teamName     the name of the team whose submission is being updated
     * @param hackathonId  the unique identifier of the hackathon
     * @param submission   the updated submission entity; must contain all required data
     * @return a status message indicating success or failure, with details if an error occurs
     */
    String updateSubmission(String teamName, long hackathonId, Submission submission);

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
}
