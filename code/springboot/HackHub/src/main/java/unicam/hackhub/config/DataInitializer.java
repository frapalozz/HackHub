package unicam.hackhub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.hackathon.model.state.HackathonStatus;
import unicam.hackhub.domain.hackathon.model.state.ProgressState;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.hackathon.repository.SubmissionRepository;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.staff.repository.StaffRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;
import unicam.hackhub.domain.utils.Period;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private HackathonRepository hackathonRepository;
    @Autowired private TeamRepository teamRepository;
    @Autowired private SubmissionRepository submissionRepository;

    private final static Staff[] STAFF = {
            new Staff("john", "john.doe@tech.com"),
            new Staff("jane", "jane.smith@innovate.com"),
            new Staff("alex", "alex.wong@ai-labs.com")
    };

    private final static User[] USERS = {
            new User("john", "john.doe@tech.com"),
            new User("jane", "jane.smith@innovate.com"),
            new User("jin", "jin.kazama@fin.com"),
    };

    private final static Team[] TEAMS = {
            new Team("team", USERS[2])
    };

    private final static Hackathon[] HACKATHONS = {

            new Hackathon(
                    "testHackathon",
                    LocalDate.now().minusDays(2),
                    new Period(LocalDate.now().minusDays(1), LocalDate.now().minusDays(1)),
                    4,
                    "",
                    5.00,
                    STAFF[0],
                    STAFF[1],
                    Arrays.stream(new Staff[]{STAFF[2]}).collect(Collectors.toSet())),
    };

    private final static Submission[] SUBMISSIONS = {
            new Submission("url.url"),
    };

    @Override
    public void run(String... args) {


        staffRepository.saveAll(Arrays.asList(STAFF));
        userRepository.saveAll(Arrays.asList(USERS));
        teamRepository.saveAll(Arrays.asList(TEAMS));

        Set<Team> teams = new HashSet<>();
        teams.add(teamRepository.findById(TEAMS[0].getName()).orElse(null));
        HACKATHONS[0].setTeams(teams);
        HACKATHONS[0].changeState(HackathonStatus.HackathonStateType.EVALUATION);
        Map<Team, Submission> sub = new HashMap<>();
        sub.put(TEAMS[0], SUBMISSIONS[0]);
        HACKATHONS[0].setSubmissions(sub);
        hackathonRepository.saveAll(Arrays.asList(HACKATHONS));
    }
}
