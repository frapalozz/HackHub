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
            throw new IllegalArgumentException("Team submission already exists");
        }
    }

    @Override
    public void updateSubmission(Team team, Submission submission) {
        if(!hackathon.getHackathonPeriod().isWithinPeriod(LocalDate.now()))
            throw new IllegalArgumentException("Hackathon not in progress");

        if(!this.hackathon.teamHasSubmission(team)) {
            throw new IllegalArgumentException("Team doesn't have a submission");
        }

        Submission oldSubmission = hackathon.getSubmission(team);
        oldSubmission.setUrl(submission.getUrl());
    }

    @Override
    public boolean inProgress() {
        return true;
    }

    @Override
    public void toNextState() {
        if(hackathon.getHackathonPeriod().endDate().equals(LocalDate.now()) ||
                hackathon.getHackathonPeriod().endDate().isBefore(LocalDate.now())) {
            hackathon.changeState(hackathon.getStatus().getNextState());
        }
    }
}
