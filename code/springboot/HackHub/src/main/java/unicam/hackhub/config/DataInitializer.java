package unicam.hackhub.config;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.hackathon.model.state.HackathonStatus;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.hackathon.repository.ReportRepository;
import unicam.hackhub.domain.hackathon.repository.SubmissionRepository;
import unicam.hackhub.domain.hackathon.repository.ValuationRepository;
import unicam.hackhub.domain.invitation.repository.InvitationRepository;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.staff.repository.StaffRepository;
import unicam.hackhub.domain.support.repository.SupportRequestRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;
import unicam.hackhub.domain.utils.Period;
import unicam.hackhub.domain.utils.Role;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    // Repositories
    private UserRepository userRepository;
    private StaffRepository staffRepository;
    private HackathonRepository hackathonRepository;
    private TeamRepository teamRepository;
    private SubmissionRepository submissionRepository;
    private InvitationRepository invitationRepository;
    private ReportRepository reportRepository;
    private SupportRequestRepository supportRequestRepository;
    private ValuationRepository valuationRepository;

    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        String encodedPassword = passwordEncoder.encode("password");

        Staff[] staffs = createStaffs(encodedPassword);
        User[] users = createUsers(encodedPassword);
        users[2].setRole(Role.TEAM_MEMBER);
        Submission[] submissions = createSubmissions();

        // Initialize users/staffs
        staffRepository.saveAll(Arrays.asList(staffs));
        List<User> usersSaved = userRepository.saveAll(Arrays.asList(users));

        // Initialize teams
        Team[] teams = createTeams(usersSaved.toArray(new User[0]));
        List<Team> teamsSaved = teamRepository.saveAll(Arrays.asList(teams));
        usersSaved.get(2).setTeam(teamsSaved.getFirst());
        userRepository.save(usersSaved.get(2));

        // Initialize hackathons
        Hackathon[] hackathons = createHackathons(staffs, teamsSaved.toArray(new Team[0]));
        List<Hackathon> hackathonsSaved = hackathonRepository.saveAll(Arrays.asList(hackathons));

        // Add submission to PROGRESS Hackathon
        Submission submission = submissionRepository.save(submissions[0]);
        Map<Team, Submission> submissionsMap = new HashMap<>();
        submissionsMap.put(teamsSaved.getFirst(), submission);
        hackathonsSaved.get(2).setSubmissions(submissionsMap);
        hackathonRepository.save(hackathonsSaved.get(2));
    }

    private User[] createUsers(String password) {
        return new User[] {
                new User("john", "john@user.com", password),
                new User("jane", "jane@user.com", password),
                new User("alex", "alex@user.com", password),
        };
    }

    private Staff[] createStaffs(String password) {
        return new Staff[] {
                new Staff("john", "john@staff.com", password),
                new Staff("jane", "jane@staff.com", password),
                new Staff("alex", "alex@staff.com", password)
        };
    }

    private Team[] createTeams(User[] users) {
        return new Team[] {
                new Team("team", users[2])
        };
    }

    private Hackathon[] createHackathons(Staff[] staffs, Team[] teams) {
        return new Hackathon[] {
                // SUBSCRIPTION Hackathon
                Hackathon.builder()
                        .name("SubscriptionHackathon")
                        .subscriptionDeadline(LocalDate.now().plusDays(1))
                        .hackathonPeriod(new Period(LocalDate.now().plusDays(2), LocalDate.now().plusDays(3)))
                        .maxTeamSize(4)
                        .requirements("Requirements hackathon")
                        .prize(50.0)
                        .organizer(staffs[0])
                        .judge(staffs[1])
                        .mentors(Arrays.stream(new Staff[]{staffs[2]}).collect(Collectors.toSet()))
                        .status(new HackathonStatus(HackathonStatus.HackathonStateType.SUBSCRIPTION))
                        .build(),
                // PROGRESS Hackathon
                Hackathon.builder()
                        .name("ProgressHackathon")
                        .subscriptionDeadline(LocalDate.now().minusDays(1))
                        .hackathonPeriod(new Period(LocalDate.now(), LocalDate.now().plusDays(1)))
                        .maxTeamSize(4)
                        .requirements("Requirements hackathon")
                        .prize(75.0)
                        .organizer(staffs[0])
                        .judge(staffs[1])
                        .mentors(Arrays.stream(new Staff[]{staffs[2]}).collect(Collectors.toSet()))
                        .teams(Set.of(teams))
                        .status(new HackathonStatus(HackathonStatus.HackathonStateType.PROGRESS))
                        .build(),
                // EVALUATION Hackathon
                Hackathon.builder()
                        .name("EvaluationHackathon")
                        .subscriptionDeadline(LocalDate.now().minusDays(2))
                        .hackathonPeriod(new Period(LocalDate.now().minusDays(1), LocalDate.now()))
                        .maxTeamSize(4)
                        .requirements("Requirements hackathon")
                        .prize(95.0)
                        .organizer(staffs[0])
                        .judge(staffs[1])
                        .mentors(Arrays.stream(new Staff[]{staffs[2]}).collect(Collectors.toSet()))
                        .teams(Set.of(teams))
                        .status(new HackathonStatus(HackathonStatus.HackathonStateType.EVALUATION))
                        .build(),
                // ENDED Hackathon
                Hackathon.builder()
                        .name("EndedHackathon")
                        .subscriptionDeadline(LocalDate.now().minusDays(2))
                        .hackathonPeriod(new Period(LocalDate.now().minusDays(1), LocalDate.now()))
                        .maxTeamSize(4)
                        .requirements("Requirements hackathon")
                        .prize(95.0)
                        .organizer(staffs[0])
                        .judge(staffs[1])
                        .mentors(Arrays.stream(new Staff[]{staffs[2]}).collect(Collectors.toSet()))
                        .status(new HackathonStatus(HackathonStatus.HackathonStateType.ENDED))
                        .build(),
        };
    }

    private Submission[] createSubmissions() {
        return new Submission[] {
                new Submission("url.url"),
        };
    }
}
