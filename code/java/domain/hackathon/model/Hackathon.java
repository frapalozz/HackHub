package code.java.domain.hackathon.model;
import java.util.Date;

import code.java.domain.hackathon.model.state.HackathonState;
import code.java.domain.utils.Period;

public class Hackathon {

    private long id;
    private String name;
    private Date subscriptionDeadline;
    private Period hackathonPeriod;
    private int maxTeamSize;
    private String requirements;
    private Double prize;
    private HackathonState state;

    private StaffMember organizer;
    private StaffMember judge;
    private Set<StaffMember> mentors;
    private Set<Team> teams;
    private Map<Team, Submission> submissions;
    private Team winner;

    public void changeState(HackathonState newState) {
        this.state = newState;
    }

    public void addTeam(Team team) {
        this.state.addTeam(team);
    }
    
    public void addSubmission(Team team, Submission submission) {
        this.state.addSubmission(team, submission);
    }

    public void updateSubmission(Team team, Submission submission) {
        this.state.updateSubmission(team, submission);
    }
}