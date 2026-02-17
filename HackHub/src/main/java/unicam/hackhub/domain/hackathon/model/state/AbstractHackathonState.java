package unicam.hackhub.domain.hackathon.model.state;

import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.team.model.Team;

public abstract class AbstractHackathonState implements HackathonState {

    protected final Hackathon hackathon;

    protected AbstractHackathonState(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    @Override
    public void registerTeam(Team team) {
        throw new IllegalStateException("Can't register team in this state");
    }

    @Override
    public void addSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Can't add submission in this state");
    }

    @Override
    public void updateSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Can't update submission in this state");
    }

    @Override
    public void valuateSubmission(String teamName, int vote, String description) {
        throw new IllegalStateException("Can't valuate submission in this state");
    }

    @Override
    public void updateValuation(String teamName, int vote, String description) {
        throw new IllegalStateException("Can't update valuation submission in this state");
    }

    @Override
    public void declareWinner(String teamName) {
        throw new IllegalStateException("Can't declare winner in this state");
    }

    @Override
    public boolean inProgress() {
        return false;
    }

    @Override
    public void toNextState() {}
}
