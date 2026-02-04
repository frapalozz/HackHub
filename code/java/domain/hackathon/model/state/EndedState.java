package code.java.domain.hackathon.model.state;

public class EndedState implements HackathonState {

    private Hackathon context;

    @Override
    public boolean addTeam(Team team) {
        throw new IllegalStateException("Hackathon ended");
    }

    @Override
    public boolean addSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Hackathon ended");
    }

    @Override
    public boolean updateSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Hackathon ended");
    }
    
}
