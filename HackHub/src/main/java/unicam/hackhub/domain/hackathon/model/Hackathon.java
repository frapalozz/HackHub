package unicam.hackhub.domain.hackathon.model;

import jakarta.persistence.*;
import lombok.*;
import unicam.hackhub.domain.hackathon.model.state.*;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.utils.Period;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
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
    private String location;

    @Embedded
    @Builder.Default
    private HackathonStatus status = new HackathonStatus(HackathonStatus.HackathonStateType.SUBSCRIPTION);

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REFRESH})
    private Staff organizer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REFRESH})
    private Staff judge;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Staff> mentors;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH})
    @Builder.Default
    private Set<Team> teams = new HashSet<>();

    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @Builder.Default
    private Map<Team, Submission> submissions = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Team winner;

    @Transient
    private HackathonState state;

    public Hackathon(String name, String location, LocalDate subDeadline, Period period, int maxsize,
                     String req, Double prize, Staff organizer, Staff judge,
                     Set<Staff> mentors) {
        this.name = name;
        this.location = location;
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


    // =====================================
    // Submission methods
    // =====================================

    public Submission getSubmission(Team team) {
        return this.submissions.get(team);
    }

    public Submission getSubmission(String teamName) {
        Team team = this.submissions
                .keySet()
                .stream()
                .filter(t -> t.getName().equals(teamName))
                .findFirst().orElse(null);

        return this.submissions.get(team);
    }

    public boolean teamHasSubmission(Team team) {
        return getSubmission(team) != null;
    }

    private void setSubmission(Team team, Submission submission) {
        this.submissions.put(team, submission);
    }

    public void addSubmission(Team team, Submission submission) {
        this.state.addSubmission(team, submission);

        this.setSubmission(team, submission);
    }

    public void updateSubmission(Team team, Submission submission) {
        this.state.updateSubmission(team, submission);
    }

    public void valuateSubmission(String teamName, int vote, String description) {
        this.state.valuateSubmission(teamName, vote, description);
    }

    public void updateValuation(String teamName, int vote, String description) {
        this.state.updateValuation(teamName, vote, description);
    }


    // =====================================
    // Team methods
    // =====================================

    public void registerTeam(Team team) {
        this.state.registerTeam(team);
        this.teams.add(team);
    }

    public boolean hasTeam(Team team) {
        return teams.contains(team);
    }

    public void declareWinner(String teamName) {
        this.state.declareWinner(teamName);

        this.changeState(HackathonStatus.HackathonStateType.ENDED);
    }


    // =====================================
    // Staff methods
    // =====================================

    public void addMentors(List<Staff> mentors) {
        this.mentors.addAll(mentors);
    }

    public boolean containsStaff(String staffEmail) {
        return organizer.getEmail().equals(staffEmail) ||
                judge.getEmail().equals(staffEmail) ||
                mentors.stream().anyMatch(m -> m.getEmail().equals(staffEmail));
    }


    // =====================================
    // State Methods
    // =====================================

    public void changeState(HackathonStatus.HackathonStateType newStateType) {
        this.status.setCurrentState(newStateType);
        this.state = HackathonStateFactory.createState(newStateType, this);
    }

    public void toNextState() {
        this.state.toNextState();
    }

    public boolean inProgress() {
        return this.state.inProgress();
    }


    // =====================================
    // Private Methods
    // =====================================

    @PostLoad
    @PostPersist
    @PostUpdate
    private void initializeStateObject() {
        this.state = HackathonStateFactory.createState(
                this.status.getCurrentState(),
                this
        );
    }
}