package code.java.domain.hackathon.model.state;

import code.java.domain.hackathon.model.Submission;
import code.java.domain.team.model.Team;

public interface HackathonState {
    
    boolean addTeam(Team team);
    boolean addSubmission(Team team, Submission submission);
    boolean updateSubmission(Team team, Submission submission);
}
