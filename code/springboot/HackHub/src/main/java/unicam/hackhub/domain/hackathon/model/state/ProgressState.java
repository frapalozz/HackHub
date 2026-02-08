package unicam.hackhub.domain.hackathon.model.state;

import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.team.model.Team;

import java.time.LocalDate;

public class ProgressState implements HackathonState {

    private final Hackathon context;

    public ProgressState(Hackathon context) {
        this.context = context;
    }

    @Override
    public void registerTeam(Team team) {
        throw new IllegalStateException("Hackathon already started");
    }

    @Override
    public void addSubmission(Team team, Submission submission) {
        if(!context.getHackathonPeriod().isWithinPeriod(LocalDate.now()))
            throw new IllegalArgumentException("Hackathon not in progress");

        if(this.context.teamHasSubmission(team)) {
            throw new IllegalArgumentException("Team have a submission");
        }
    }

    @Override
    public void updateSubmission(Team team, Submission submission) {
        if(!context.getHackathonPeriod().isWithinPeriod(LocalDate.now()))
            throw new IllegalArgumentException("Hackathon not in progress");

        if(!this.context.teamHasSubmission(team)) {
            throw new IllegalArgumentException("Team doesn't have a submission");
        }
    }

    @Override
    public boolean active() {
        return false;
    }

    @Override
    public void valuateSubmission(String teamName, int vote, String description) {
        throw new IllegalStateException("Hackathon not in evaluation");
    }

    @Override
    public void declareWinner(Team team) {
        throw new IllegalStateException("Hackathon in progress");
    }

}
