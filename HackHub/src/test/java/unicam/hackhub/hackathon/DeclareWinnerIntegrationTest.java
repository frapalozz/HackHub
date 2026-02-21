package unicam.hackhub.hackathon;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import unicam.hackhub.config.DataInitializer;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.hackathon.model.Valuation;
import unicam.hackhub.domain.hackathon.model.state.HackathonStatus;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.staff.repository.StaffRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;
import unicam.hackhub.domain.utils.Period;
import unicam.hackhub.domain.utils.Role;
import unicam.hackhub.infrastructure.security.JwtTokenUtil;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class DeclareWinnerIntegrationTest {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private HackathonRepository hackathonRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @MockitoBean
    private DataInitializer dataInitializer;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    MockMvc mockMvc;

    private final String path = "/api/v1/staff/hackathon/";

    @Test
    void declareWinner_Success_ShouldReturn200() throws Exception {
        // ARRANGE
        Staff organizer = staffRepository.save(new Staff("organizer", "organizer@test.it", "password"));
        Team team = createTestTeam(createUser("Gino","gino@gino.it"));
        Hackathon hackathon = createTestHackathon(
                LocalDate.now().minusDays(3),
                new Period(LocalDate.now().minusDays(2),LocalDate.now().minusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.EVALUATION),
                team,
                new Submission("test.url"),
                organizer, true);

        // ACT
        mockMvc.perform(post(path+hackathon.getId()+"?teamName="+team.getName())
                    .header("Authorization","Bearer "+getToken(organizer))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("winner added"));

        // ASSERT
        Hackathon hackathonResult = hackathonRepository.findById(hackathon.getId())
                .orElseThrow(IllegalStateException::new);
        assertThat(hackathonResult.getWinner()).isEqualTo(team);
        assertThat(hackathonResult.getStatus().getCurrentState()).isEqualTo(HackathonStatus.HackathonStateType.ENDED);
        assertThat(team.getBalance()).isEqualTo(hackathonResult.getPrize());
    }

    @Test
    void declareWinner_HackathonNotInEvaluation_ShouldReturn409() throws Exception {
        // ARRANGE
        Staff organizer = staffRepository.save(new Staff("organizer", "organizer@test.it", "password"));
        Team team = createTestTeam(createUser("Gino","gino@gino.it"));
        Hackathon hackathon = createTestHackathon(
                LocalDate.now().minusDays(3),
                new Period(LocalDate.now(),LocalDate.now().plusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.PROGRESS),
                team,
                new Submission("test.url"),
                organizer, true);

        // ACT
        mockMvc.perform(post(path+hackathon.getId()+"?teamName="+team.getName())
                        .header("Authorization","Bearer "+getToken(organizer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Can't declare winner in this state"));

        // ASSERT
        Hackathon hackathonResult = hackathonRepository.findById(hackathon.getId())
                .orElseThrow(IllegalStateException::new);
        assertThat(hackathonResult.getWinner()).isNull();
    }

    @Test
    void declareWinner_HackathonNotFound_ShouldReturn404() throws Exception {
        // ARRANGE
        Staff organizer = staffRepository.save(new Staff("organizer", "organizer@test.it", "password"));
        Team team = createTestTeam(createUser("Gino","gino@gino.it"));
        Hackathon hackathon = createTestHackathon(
                LocalDate.now().minusDays(3),
                new Period(LocalDate.now().minusDays(2),LocalDate.now().minusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.EVALUATION),
                team,
                new Submission("test.url"),
                organizer, true);

        // ACT
        mockMvc.perform(post(path+(hackathon.getId()+999)+"?teamName="+team.getName())
                        .header("Authorization","Bearer "+getToken(organizer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Hackathon not found"));
    }

    @Test
    void declareWinner_TeamNotFound_ShouldReturn404() throws Exception {
        // ARRANGE
        Staff organizer = staffRepository.save(new Staff("organizer", "organizer@test.it", "password"));
        Team team = createTestTeam(createUser("Gino","gino@gino.it"));
        Hackathon hackathon = createTestHackathon(
                LocalDate.now().minusDays(3),
                new Period(LocalDate.now().minusDays(2),LocalDate.now().minusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.EVALUATION),
                team,
                new Submission("test.url"),
                organizer, true);

        // ACT
        mockMvc.perform(post(path+hackathon.getId()+"?teamName="+team.getName()+"ggg")
                        .header("Authorization","Bearer "+getToken(organizer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Team not found"));

        // ASSERT
        Hackathon hackathonResult = hackathonRepository.findById(hackathon.getId())
                .orElseThrow(IllegalStateException::new);
        assertThat(hackathonResult.getWinner()).isNull();
    }

    @Test
    void declareWinner_NotAllSubmissionValuated_ShouldReturn409() throws Exception {
        // ARRANGE
        Staff organizer = staffRepository.save(new Staff("organizer", "organizer@test.it", "password"));
        Team team = createTestTeam(createUser("Gino","gino@gino.it"));
        Hackathon hackathon = createTestHackathon(
                LocalDate.now().minusDays(3),
                new Period(LocalDate.now().minusDays(2),LocalDate.now().minusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.EVALUATION),
                team,
                new Submission("test.url"),
                organizer, false);

        // ACT
        mockMvc.perform(post(path+hackathon.getId()+"?teamName="+team.getName())
                        .header("Authorization","Bearer "+getToken(organizer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Missing valuation"));

        // ASSERT
        Hackathon hackathonResult = hackathonRepository.findById(hackathon.getId())
                .orElseThrow(IllegalStateException::new);
        assertThat(hackathonResult.getWinner()).isNull();
    }

    private Hackathon createTestHackathon(LocalDate subDeadline,
                                          Period period,
                                          HackathonStatus state,
                                          Team team,
                                          Submission sub,
                                          Staff organizer,
                                          boolean val) {
        Staff judge = new Staff("judge", "judge@test.it", "password");
        Staff mentor1 = new Staff("mentor1", "mentor1@test.it", "password");
        Staff mentor2 = new Staff("mentor2", "mentor2@test.it", "password");

        // SALVA gli staff prima di usarli nell'hackathon
        staffRepository.save(organizer);
        staffRepository.save(mentor1);
        staffRepository.save(mentor2);

        if(val)
            sub.setValuation(new Valuation(5, "descr"));

        Set<Staff> mentors = new HashSet<>();
        mentors.add(mentor1);
        mentors.add(mentor2);

        Map<Team, Submission> submissions = new HashMap<>();
        if(sub != null) {
            submissions.put(team, sub);
        }


        Hackathon hackathon = Hackathon.builder()
                .name("Hackathon Test")
                .subscriptionDeadline(subDeadline)
                .hackathonPeriod(period)
                .maxTeamSize(4)
                .requirements("Regolamento...")
                .prize(1000.0)
                .organizer(organizer)
                .judge(judge)
                .teams(team == null ? new HashSet<>() : Set.of(team))
                .submissions(submissions)
                .mentors(mentors)
                .status(state)
                .build();

        return hackathonRepository.save(hackathon);
    }

    private User createUser(String name, String email) {
        User user = new User(name, email, "password");
        user.setRole(Role.TEAM_MEMBER);
        return userRepository.save(user);
    }

    private Team createTestTeam(User user) {
        User user2 = createUser("user2", "user2@test.it");
        Team team = new Team("teammmm", user);
        team.addMember(user2);
        team = teamRepository.save(team);
        user.setTeam(team);
        user2.setTeam(team);
        userRepository.save(user);
        userRepository.save(user2);
        return team;
    }

    private String getToken(Staff user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        return jwtTokenUtil.generateToken(userDetails);
    }
}
