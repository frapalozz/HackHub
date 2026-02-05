package code.java.src.domain.hackathon.model;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import code.java.src.domain.hackathon.model.state.HackathonState;
import code.java.src.domain.staffMember.model.Judge;
import code.java.src.domain.staffMember.model.Mentor;
import code.java.src.domain.staffMember.model.Organizer;
import code.java.src.domain.team.model.Team;
import code.java.src.domain.utils.Period;

public class Hackathon {

    private long id;
    private String name;
    private Date subscriptionDeadline;
    private Period hackathonPeriod;
    private int maxTeamSize;
    private String requirements;
    private Double prize;
    private HackathonState state;

    private Organizer organizer;
    private Judge judge;
    private Set<Mentor> mentors;
    private Set<Team> teams;
    private Map<Team, Submission> submissions;
    private Team winner;

    public long getId() {
        return this.id;
    }

    public void changeState(HackathonState newState) {
        this.state = newState;
    }

    public void addTeam(Team team) {
        if(!this.state.addTeam(team)) return;

        this.teams.add(team);
    }
    
    public void addSubmission(Team team, Submission submission) {
        if(!this.state.addSubmission(team, submission)) return;

        this.setSubmission(team, submission);
    }

    public void updateSubmission(Team team, Submission submission) {
        if(!this.state.updateSubmission(team, submission)) return;

        this.setSubmission(team, submission);
    }

    public boolean hasTeam(Team team) {
        return teams.contains(team);
    }

    public boolean teamHasSubmission(Team team) {
        return submissions.get(team) != null;
    }

    private void setSubmission(Team team, Submission submission) {
        this.submissions.put(team, submission);
    }
}