package plainjava.src.domain.hackathon.model.state;

import plainjava.src.domain.hackathon.model.Submission;
import plainjava.src.domain.team.model.Team;

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
     * Check if a hackathon is active
     * @return true if hackathon is active, false otherwise
     */
    boolean active();
}
