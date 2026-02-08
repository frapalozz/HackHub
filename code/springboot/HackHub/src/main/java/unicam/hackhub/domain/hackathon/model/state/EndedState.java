package unicam.hackhub.domain.hackathon.model.state;

import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.team.model.Team;

public class EndedState implements HackathonState {

    @Override
    public void registerTeam(Team team) {
        throw new IllegalStateException("Hackathon ended");
    }

    @Override
    public void addSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Hackathon ended");
    }

    @Override
    public void updateSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Hackathon ended");
    }

    @Override
    public boolean active() {
        return false;
    }

    @Override
    public void valuateSubmission(String teamName, int vote, String description) {
        throw new IllegalStateException("Hackathon ended");
    }

    @Override
    public void declareWinner(Team team) {
        throw new IllegalStateException("Hackathon ended");
    }

}
