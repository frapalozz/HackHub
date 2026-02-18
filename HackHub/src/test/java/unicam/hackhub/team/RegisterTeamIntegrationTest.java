package unicam.hackhub.team;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import unicam.hackhub.application.team.RegisterTeamHandler;
import unicam.hackhub.config.DataInitializer;
import unicam.hackhub.domain.hackathon.model.Hackathon;
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
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class RegisterTeamIntegrationTest {

    @Autowired
    private RegisterTeamHandler registerTeamHandler;
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

    private final String path = "http://localhost:8888/api/v1/team/register";

    @Test
    void registerTeam_success_shouldAddTeamToHackathon() throws Exception {
        // ARRANGE
        Hackathon hackathon = createTestHackathon(
                LocalDate.now().plusDays(2),
                new Period(LocalDate.now().plusDays(3), LocalDate.now().plusDays(4)),
                4,
                new HackathonStatus(HackathonStatus.HackathonStateType.SUBSCRIPTION)
        );
        User user = createUser("user1", "user1@test.it");
        Team team = createTestTeam("TeamVincente", user);

        // ACT
        mockMvc.perform(post(path + "/" + hackathon.getId())
                        .header("Authorization", "Bearer " + getToken(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Team registered to hackathon (id: "+hackathon.getId()+")"));

        // ASSERT
        Hackathon updatedHackathon = hackathonRepository.findById(hackathon.getId()).orElseThrow();
        assertThat(updatedHackathon.getTeams()).contains(team);
    }

    @Test
    void registerTeam_HackathonNotFound_shouldThrowException() throws Exception {
        // ARRANGE
        User user = createUser("user1", "user1@test.it");
        Team team = createTestTeam("TeamEsistente", user);

        // ACT & ASSERT
        // hackathon inesistente
        mockMvc.perform(post(path + "/99999999")
                        .header("Authorization", "Bearer " + getToken(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Hackathon not found"));
    }

    @Test
    void registerTeam_subscriptionClosed_shouldThrowException() throws Exception {
        // ARRANGE
        Hackathon hackathon = createTestHackathon(
                LocalDate.now().minusDays(1),           // scaduta (oggi è 2026-02-12)
                new Period(LocalDate.now(), LocalDate.now().plusDays(2)),
                4,
                new HackathonStatus(HackathonStatus.HackathonStateType.PROGRESS)
        );
        User user = createUser("user1", "user1@test.it");
        Team team = createTestTeam("TeamInRitardo", user);

        // ACT & ASSERT
        mockMvc.perform(post(path + "/" + hackathon.getId())
                        .header("Authorization", "Bearer " + getToken(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Can't register team in this state"));

        assertThat(hackathon.getTeams()).doesNotContain(team);
    }

    @Test
    void registerTeam_teamTooLarge_shouldThrowException() throws Exception {
        // ARRANGE
        Hackathon hackathon = createTestHackathon(
                LocalDate.now().plusDays(1),
                new Period(LocalDate.now().plusDays(2), LocalDate.now().plusDays(3)),
                1,
                new HackathonStatus(HackathonStatus.HackathonStateType.SUBSCRIPTION)
        );

        User user = createUser("user1", "user1@test.it");
        Team team = createTestTeam("TeamTroppoGrande", user);

        // ACT & ASSERT
        mockMvc.perform(post(path + "/" + hackathon.getId())
                        .header("Authorization", "Bearer " + getToken(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Max team size exceeded"));

        assertThat(hackathon.getTeams()).doesNotContain(team);
    }

    @Test
    void registerTeam_teamAlreadyRegistered_shouldThrowException() throws Exception {
        // ARRANGE
        Hackathon hackathon = createTestHackathon(
                LocalDate.now().plusDays(2),
                new Period(LocalDate.now().plusDays(3), LocalDate.now().plusDays(4)),
                4,
                new HackathonStatus(HackathonStatus.HackathonStateType.SUBSCRIPTION)
        );

        User user = createUser("user1", "user1@test.it");
        Team team = createTestTeam("TeamRegistrato", user);

        // Prima registrazione → successo
        mockMvc.perform(post(path + "/" + hackathon.getId())
                        .header("Authorization", "Bearer " + getToken(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Team registered to hackathon (id: "+hackathon.getId()+")"));

        // ASSERT
        Hackathon updatedHackathon = hackathonRepository.findById(hackathon.getId()).orElseThrow();
        assertThat(updatedHackathon.getTeams()).contains(team);

        mockMvc.perform(post(path + "/" + hackathon.getId())
                        .header("Authorization", "Bearer " + getToken(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Team already present"));
    }

    private Hackathon createTestHackathon(LocalDate subscriptionDeadline, Period period, int max, HackathonStatus state) {
        Staff organizer = new Staff("organizer", "organizer@test.it", "password");
        Staff judge = new Staff("judge", "judge@test.it", "password");
        Staff mentor1 = new Staff("mentor1", "mentor1@test.it", "password");
        Staff mentor2 = new Staff("mentor2", "mentor2@test.it", "password");

        // SALVA gli staff prima di usarli nell'hackathon
        staffRepository.save(organizer);
        staffRepository.save(judge);
        staffRepository.save(mentor1);
        staffRepository.save(mentor2);

        Set<Staff> mentors = new HashSet<>();
        mentors.add(mentor1);
        mentors.add(mentor2);

        Hackathon hackathon = Hackathon.builder()
                .name("Hackathon Test")
                .subscriptionDeadline(subscriptionDeadline)
                .hackathonPeriod(period)
                .maxTeamSize(max)
                .requirements("Regolamento...")
                .prize(1000.0)
                .organizer(organizer)
                .judge(judge)
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

    private Team createTestTeam(String teamName, User user) {
        User user2 = createUser("user2", "user2@test.it");
        Team team = new Team(teamName, user);
        team.addMember(user2);
        team = teamRepository.save(team);
        user.setTeam(team);
        user2.setTeam(team);
        userRepository.save(user);
        userRepository.save(user2);
        return team;
    }

    private String getToken(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        return jwtTokenUtil.generateToken(userDetails);
    }
}
