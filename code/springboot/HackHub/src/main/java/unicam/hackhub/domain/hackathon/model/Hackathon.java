package unicam.hackhub.domain.hackathon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import unicam.hackhub.domain.hackathon.model.state.*;
import unicam.hackhub.domain.staffMember.model.StaffMember;
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

    @Column(nullable = false)
    private LocalDate subscriptionDeadline;

    @Embedded
    private Period hackathonPeriod;

    @Column(nullable = false)
    private int maxTeamSize;

    @Column(columnDefinition = "TEXT")
    private String requirements;
    private Double prize;

    @Embedded
    private HackathonStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private StaffMember organizer;

    @ManyToOne(fetch = FetchType.LAZY)
    private StaffMember judge;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<StaffMember> mentors;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Team> teams;

    @OneToMany(cascade = CascadeType.ALL)
    private Map<Team, Submission> submissions;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team winner;

    @Transient
    private HackathonState state;

    public Hackathon(String name, LocalDate subDeadline, Period period, int maxsize,
                     String req, Double prize, StaffMember organizer, StaffMember judge,
                     Set<StaffMember> mentors) {
        this.name = name;
        this.subscriptionDeadline = subDeadline;
        this.hackathonPeriod = period;
        this.maxTeamSize = maxsize;
        this.requirements = req;
        this.prize = prize;
        this.organizer = organizer;
        this.judge = judge;
        this.mentors = mentors;

        this.status = new HackathonStatus();
        this.teams = new HashSet<>();
        this.submissions = new HashMap<>();
        initializeStateObject();
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    private void initializeStateObject() {
        this.state = HackathonStateFactory.createState(
                this.status.getCurrentState(),
                this
        );
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

        this.changeState(HackathonStatus.HackathonStateType.ENDED);
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

    public void changeState(HackathonStatus.HackathonStateType newStateTyoe) {
        this.status.setCurrentState(newStateTyoe);
        this.state = HackathonStateFactory.createState(newStateTyoe, this);
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