package unicam.hackhub.domain.hackathon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import unicam.hackhub.domain.hackathon.model.state.EndedState;
import unicam.hackhub.domain.hackathon.model.state.HackathonState;
import unicam.hackhub.domain.hackathon.model.state.SubscriptionState;
import unicam.hackhub.domain.staffMember.model.Judge;
import unicam.hackhub.domain.staffMember.model.Mentor;
import unicam.hackhub.domain.staffMember.model.Organizer;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.utils.Period;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Hackathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "subscription_deadline", nullable = false)
    private LocalDate subscriptionDeadline;

    @Embedded
    private Period hackathonPeriod;

    @Column(name = "max_team_size", nullable = false)
    private int maxTeamSize;

    @Column(columnDefinition = "TEXT")
    private String requirements;
    private Double prize;

    @Embedded
    private HackathonState state;

    private Organizer organizer;
    private Judge judge;
    private Set<Mentor> mentors;
    private Set<Team> teams;
    private Map<Team, Submission> submissions;
    private Team winner;

    public Hackathon(String name, LocalDate subDeadline, Period period, int maxsize,
                     String req, Double prize, Organizer organizer, Judge judge,
                     Set<Mentor> mentors) {
        this.name = name;
        this.subscriptionDeadline = subDeadline;
        this.hackathonPeriod = period;
        this.maxTeamSize = maxsize;
        this.requirements = req;
        this.prize = prize;
        this.organizer = organizer;
        this.judge = judge;
        this.mentors = mentors;

        this.state = new SubscriptionState(this);
        this.teams = new HashSet<>();
        this.submissions = new HashMap<>();
    }

    public Submission getSubmission(Team team) {
        return this.submissions.get(team);
    }

    public void declareWinner(String teamName) {
        Team team = this.teams.stream()
                .filter(t -> t.getName().equals(teamName))
                .findFirst().orElse(null);

        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }

        this.state.declareWinner(team);

        this.changeState(new EndedState());
    }

    public Submission getSubmission(String teamName) {
        Team team = this.submissions
                .keySet()
                .stream()
                .filter(t -> t.getName().equals(teamName))
                .findFirst().orElse(null);

        if (team == null) {
            throw new IllegalArgumentException("Team not found");
        }

        return this.submissions.get(team);
    }

    public boolean active() {
        return state.active();
    }

    public void valuateSubmission(String teamName, int vote, String description) {
        this.state.valuateSubmission(teamName, vote, description);
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