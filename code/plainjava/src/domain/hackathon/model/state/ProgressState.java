package plainjava.src.domain.hackathon.model.state;

import plainjava.src.domain.hackathon.model.Hackathon;
import plainjava.src.domain.hackathon.model.Submission;
import plainjava.src.domain.team.model.Team;

import java.time.LocalDate;

public class ProgressState implements HackathonState {

    private Hackathon context;

    public ProgressState(Hackathon context) {
        this.context = context;
    }

    @Override
    public boolean registerTeam(Team team) {
        throw new IllegalStateException("Hackathon already started");
    }

    @Override
    public boolean addSubmission(Team team, Submission submission) {
        if(this.context.teamHasSubmission(team)) {
            throw new IllegalArgumentException("Team have a submission");
        }
        return context.getHackathonPeriod().isWithinPeriod(LocalDate.now());
    }

    @Override
    public boolean updateSubmission(Team team, Submission submission) {
        if(!this.context.teamHasSubmission(team)) {
            throw new IllegalArgumentException("Team doesn't have a submission");
        }
        return context.getHackathonPeriod().isWithinPeriod(LocalDate.now());
    }
    
}
