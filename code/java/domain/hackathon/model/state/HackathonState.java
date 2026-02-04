package code.java.domain.hackathon.model.state;

public interface HackathonState {
    
    void addTeam(Team team);
    void addSubmission(Team team, Submission submission);
    void updateSubmission(Team team, Submission submission);
}
