package plainjava.src.domain.hackathon.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import plainjava.src.domain.hackathon.model.state.HackathonState;
import plainjava.src.domain.hackathon.model.state.SubscriptionState;
import plainjava.src.domain.staffMember.model.Judge;
import plainjava.src.domain.staffMember.model.Mentor;
import plainjava.src.domain.staffMember.model.Organizer;
import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.utils.Period;

public class Hackathon {

    private long id;
    private String name;
    private LocalDate subscriptionDeadline;
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

    public Hackathon(String name, LocalDate subDeadline, Period period, int mxtsize, String req, Double prize, Organizer organizer, Judge judge, Set<Mentor> mentors) {
        this.name = name;
        this.subscriptionDeadline = subDeadline;
        this.hackathonPeriod = period;
        this.maxTeamSize = mxtsize;
        this.requirements = req;
        this.prize = prize;
        this.organizer = organizer;
        this.judge = judge;
        this.mentors = mentors;

        this.state = new SubscriptionState(this);
        this.teams = new HashSet<>();
        this.submissions = new HashMap<>();
    }

    public long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Team> getTeams() {
        return this.teams;
    }

    public int getMaxTeamSize() {
        return this.maxTeamSize;
    }

    public Period getHackathonPeriod() {
        return this.hackathonPeriod;
    }

    public LocalDate getSubscriptionDeadline() {
        return this.subscriptionDeadline;
    }

    public Submission getSubmission(Team team) {
        return this.submissions.get(team);
    }

    public boolean active() {
        return state.active();
    }

    public void changeState(HackathonState newState) {
        this.state = newState;
    }

    public void registerTeam(Team team) {
        this.state.registerTeam(team);
        this.teams.add(team);
    }
    
    public void addSubmission(Team team, Submission submission) {
        this.state.addSubmission(team, submission);

        this.setSubmission(team, submission);
    }

    public void updateSubmission(Team team, Submission submission) {
        this.state.updateSubmission(team, submission);

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