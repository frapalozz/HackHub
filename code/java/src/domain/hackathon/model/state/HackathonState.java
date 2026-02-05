package code.java.src.domain.hackathon.model.state;

import code.java.src.domain.hackathon.model.Submission;
import code.java.src.domain.team.model.Team;

public interface HackathonState {
    
    boolean registerTeam(Team team);
    boolean addSubmission(Team team, Submission submission);
    boolean updateSubmission(Team team, Submission submission);
}
