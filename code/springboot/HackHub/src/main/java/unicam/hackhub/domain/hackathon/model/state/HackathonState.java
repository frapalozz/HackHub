package unicam.hackhub.domain.hackathon.model.state;

import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.team.model.Team;

public interface HackathonState {

    /**
     * Register a team
     * @param team to register
     */
    void registerTeam(Team team);

    /**
     * Add a submission for a team
     * @param team to add the submission for
     * @param submission the submission to add
     */
    void addSubmission(Team team, Submission submission);

    /**
     * Update the submission for a team
     * @param team to update the submission for
     * @param submission the new submission
     */
    void updateSubmission(Team team, Submission submission);

    /**
     * Valuate a submission
     * @param teamName team to valuate the submission for
     * @param vote vote of the valuation
     * @param description description of the valuation
     */
    void valuateSubmission(String teamName, int vote, String description);

    /**
     * Valuate a submission
     * @param teamName team to valuate the submission for
     * @param vote vote of the valuation
     * @param description description of the valuation
     */
    void updateValuation(String teamName, int vote, String description);

    /**
     * Declare the team winner
     * @param teamName the winning team
     */
    void declareWinner(String teamName);

    /**
     * Return true if the hackathon is in PROGRESS
     * @return true if the hackathon is in PROGRESS, otherwise false
     */
    boolean inProgress();
}
