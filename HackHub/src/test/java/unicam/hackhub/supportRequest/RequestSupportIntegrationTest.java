package unicam.hackhub.supportRequest;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import unicam.hackhub.application.supportRequest.CalendarHandler;
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
import unicam.hackhub.domain.utils.TimeRange;
import unicam.hackhub.infrastructure.security.JwtTokenUtil;
import unicam.hackhub.presentation.dto.request.SupportRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class RequestSupportIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    HackathonRepository hackathonRepository;
    @Autowired
    CalendarHandler calendarHandler;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @MockitoBean
    DataInitializer dataInitializer;

    private final String path = "/api/v1/team/support";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void requestReport_Success_ShouldReturn201() throws Exception {
        // ARRANGE
        User user = createUser("test", "test@test.test");
        Team team = createTestTeam(user);
        Hackathon hackathon = createTestHackathon(LocalDate.now().minusDays(1)
                ,new Period(LocalDate.now(),LocalDate.now().plusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.PROGRESS),
                team);

        SupportRequest req = new SupportRequest(
                hackathon.getId(),
                "mentor1@test.it",
                new TimeRange(LocalTime.of(13, 0), LocalTime.of(14, 0)),
                LocalDate.now().plusDays(1)
        );

        // ACT
        mockMvc.perform(post(path)
                    .header("Authorization","Bearer "+getToken(user))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Request created"));

        // ASSERT
        assertThat(calendarHandler
                .getFreeSlots("mentor1@test.it", LocalDate.now().plusDays(1))
                .stream()
                .noneMatch(t -> t.getStartTime().equals(LocalTime.of(13, 0)) &&
                                           t.getEndTime().equals(LocalTime.of(14, 0)))).isTrue();
    }

    @Test
    void requestReport_TeamNotInHackathon_ShouldReturn400() throws Exception {
        // ARRANGE
        User user = createUser("test", "test@test.test");
        Team team = createTestTeam(user);
        Hackathon hackathon = createTestHackathon(LocalDate.now().minusDays(1)
                ,new Period(LocalDate.now(),LocalDate.now().plusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.PROGRESS),
                null);

        SupportRequest req = new SupportRequest(
                hackathon.getId(),
                "mentor1@test.it",
                new TimeRange(LocalTime.of(13, 0), LocalTime.of(14, 0)),
                LocalDate.now().plusDays(1)
        );

        // ACT
        mockMvc.perform(post(path)
                        .header("Authorization","Bearer "+getToken(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Team not in hackathon"));

        // ASSERT
        assertThat(calendarHandler
                .getFreeSlots("mentor1@test.it", LocalDate.now().plusDays(1))
                .stream()
                .noneMatch(t -> t.getStartTime().equals(LocalTime.of(13, 0)) &&
                        t.getEndTime().equals(LocalTime.of(14, 0)))).isFalse();
    }

    @Test
    void requestReport_OutsideWorkingHours_ShouldReturn400() throws Exception {
        // ARRANGE
        User user = createUser("test", "test@test.test");
        Team team = createTestTeam(user);
        Hackathon hackathon = createTestHackathon(LocalDate.now().minusDays(1)
                ,new Period(LocalDate.now(),LocalDate.now().plusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.PROGRESS),
                team);

        SupportRequest req = new SupportRequest(
                hackathon.getId(),
                "mentor1@test.it",
                new TimeRange(LocalTime.of(22, 0), LocalTime.of(23, 0)),
                LocalDate.now().plusDays(1)
        );

        // ACT
        mockMvc.perform(post(path)
                        .header("Authorization","Bearer "+getToken(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Slot outside working hours"));

        // ASSERT
        assertThat(calendarHandler
                .getFreeSlots("mentor1@test.it", LocalDate.now().plusDays(1))
                .stream()
                .noneMatch(t -> t.getStartTime().equals(LocalTime.of(22, 0)) &&
                        t.getEndTime().equals(LocalTime.of(23, 0)))).isTrue();
    }

    @Test
    void requestReport_SlotOccupied_ShouldReturn409() throws Exception {
        // ARRANGE
        User user = createUser("test", "test@test.test");
        Team team = createTestTeam(user);
        Hackathon hackathon = createTestHackathon(LocalDate.now().minusDays(1)
                ,new Period(LocalDate.now(),LocalDate.now().plusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.PROGRESS),
                team);

        SupportRequest req = new SupportRequest(
                hackathon.getId(),
                "mentor1@test.it",
                new TimeRange(LocalTime.of(13, 0), LocalTime.of(14, 0)),
                LocalDate.now().plusDays(1)
        );

        // ACT
        mockMvc.perform(post(path)
                        .header("Authorization","Bearer "+getToken(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Request created"));

        // ASSERT
        assertThat(calendarHandler
                .getFreeSlots("mentor1@test.it", LocalDate.now().plusDays(1))
                .stream()
                .noneMatch(t -> t.getStartTime().equals(LocalTime.of(13, 0)) &&
                        t.getEndTime().equals(LocalTime.of(14, 0)))).isTrue();

        // ACT
        mockMvc.perform(post(path)
                        .header("Authorization","Bearer "+getToken(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Slot already occupied"));

        // ASSERT
        assertThat(calendarHandler
                .getFreeSlots("mentor1@test.it", LocalDate.now().plusDays(1))
                .stream()
                .noneMatch(t -> t.getStartTime().equals(LocalTime.of(13, 0)) &&
                        t.getEndTime().equals(LocalTime.of(14, 0)))).isTrue();
    }

    @Test
    void requestReport_MentorNotFound_ShouldReturn404() throws Exception {
        // ARRANGE
        User user = createUser("test", "test@test.test");
        Team team = createTestTeam(user);
        Hackathon hackathon = createTestHackathon(LocalDate.now().minusDays(1)
                ,new Period(LocalDate.now(),LocalDate.now().plusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.PROGRESS),
                team);

        SupportRequest req = new SupportRequest(
                hackathon.getId(),
                "mentor9999@test.it",
                new TimeRange(LocalTime.of(13, 0), LocalTime.of(14, 0)),
                LocalDate.now().plusDays(1)
        );

        // ACT
        mockMvc.perform(post(path)
                        .header("Authorization","Bearer "+getToken(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Mentor not found"));

        // ASSERT
        assertThat(calendarHandler
                .getFreeSlots("mentor1@test.it", LocalDate.now().plusDays(1))
                .stream()
                .noneMatch(t -> t.getStartTime().equals(LocalTime.of(13, 0)) &&
                        t.getEndTime().equals(LocalTime.of(14, 0)))).isFalse();
    }

    @Test
    void requestReport_HackathonNotInProgress_ShouldReturn409() throws Exception {
        // ARRANGE
        User user = createUser("test", "test@test.test");
        Team team = createTestTeam(user);
        Hackathon hackathon = createTestHackathon(LocalDate.now().minusDays(3)
                ,new Period(LocalDate.now().minusDays(2),LocalDate.now().minusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.ENDED),
                team);

        SupportRequest req = new SupportRequest(
                hackathon.getId(),
                "mentor1@test.it",
                new TimeRange(LocalTime.of(13, 0), LocalTime.of(14, 0)),
                LocalDate.now().plusDays(1)
        );

        // ACT
        mockMvc.perform(post(path)
                        .header("Authorization","Bearer "+getToken(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Hackathon not in progress"));

        // ASSERT
        assertThat(calendarHandler
                .getFreeSlots("mentor1@test.it", LocalDate.now().plusDays(1))
                .stream()
                .noneMatch(t -> t.getStartTime().equals(LocalTime.of(13, 0)) &&
                        t.getEndTime().equals(LocalTime.of(14, 0)))).isFalse();
    }


    private Hackathon createTestHackathon(LocalDate subscriptionDeadline, Period period, HackathonStatus state, Team team) {
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
                .maxTeamSize(4)
                .requirements("Regolamento...")
                .prize(1000.0)
                .organizer(organizer)
                .judge(judge)
                .mentors(mentors)
                .teams(team != null ? Set.of(team) : new HashSet<>())
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
        Team team = new Team("test", user);
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
