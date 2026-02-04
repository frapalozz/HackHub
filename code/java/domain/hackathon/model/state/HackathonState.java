package code.java.domain.hackathon.model.state;

public interface HackathonState {
    
    boolean addTeam(Team team);
    boolean addSubmission(Team team, Submission submission);
    boolean updateSubmission(Team team, Submission submission);
}
