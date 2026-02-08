package unicam.hackhub.domain.hackathon.model.state;

import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.team.model.Team;

import java.time.LocalDate;

public class ProgressState extends AbstractHackathonState {

    public ProgressState(Hackathon context) {
        super(context);
    }

    @Override
    public void addSubmission(Team team, Submission submission) {
        if(!hackathon.getHackathonPeriod().isWithinPeriod(LocalDate.now()))
            throw new IllegalArgumentException("Hackathon not in progress");

        if(this.hackathon.teamHasSubmission(team)) {
            throw new IllegalArgumentException("Team have a submission");
        }
    }

    @Override
    public void updateSubmission(Team team, Submission submission) {
        if(!hackathon.getHackathonPeriod().isWithinPeriod(LocalDate.now()))
            throw new IllegalArgumentException("Hackathon not in progress");

        if(!this.hackathon.teamHasSubmission(team)) {
            throw new IllegalArgumentException("Team doesn't have a submission");
        }
    }

    @Override
    public boolean active() {
        return true;
    }
}
