package unicam.hackhub.submission;

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
import unicam.hackhub.config.DataInitializer;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.state.HackathonStatus;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.hackathon.repository.SubmissionRepository;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.staff.repository.StaffRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;
import unicam.hackhub.domain.utils.Period;
import unicam.hackhub.domain.utils.Role;
import unicam.hackhub.infrastructure.security.JwtTokenUtil;
import unicam.hackhub.presentation.dto.request.SubmissionRequest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class AddSubmissionIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @MockitoBean
    DataInitializer dataInitializer;
    private final String path = "/api/v1/team/hackathon/";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private HackathonRepository hackathonRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addSubmission_Success_ShouldReturn201() throws Exception {
        Hackathon hackathon = createTestHackathon(LocalDate.now().minusDays(1)
        ,new Period(LocalDate.now(),LocalDate.now().plusDays(1)),
                4,new HackathonStatus(HackathonStatus.HackathonStateType.PROGRESS));
        User user = createUser("Gino","gino@gino.it");
        Team team = createTestTeam("IGiniss",user);

        SubmissionRequest sub = new SubmissionRequest("https://ciao.site");

        mockMvc.perform(post(path+hackathon.getId())
                .header("Authorization","Bearer "+getToken(user))
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sub)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Submission added"));

        Hackathon a = hackathonRepository.findById(hackathon.getId()).get();
        assertThat(a.getSubmission(team.getName())).isNotNull();
    }

    @Test
    void addSubmission_AlreadyPresent_ShouldReturn409() throws Exception {
        Hackathon hackathon = createTestHackathon(LocalDate.now().minusDays(1)
                ,new Period(LocalDate.now(),LocalDate.now().plusDays(1)),
                4,new HackathonStatus(HackathonStatus.HackathonStateType.PROGRESS));
        User user = createUser("Gino","gino@gino.it");
        Team team = createTestTeam("IGiniss",user);

        SubmissionRequest sub = new SubmissionRequest("https://ciao.site");

        mockMvc.perform(post(path+hackathon.getId())
                        .header("Authorization","Bearer "+getToken(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sub)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Submission added"));

        mockMvc.perform(post(path+hackathon.getId())
                        .header("Authorization","Bearer "+getToken(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sub)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Team submission already exists"));

        Hackathon a = hackathonRepository.findById(hackathon.getId()).get();
        assertThat(a.getSubmission(team.getName())).isNotNull();
    }

    @Test
    void addSubmission_InvalidURL_ShouldReturn400() throws Exception {
        Hackathon hackathon = createTestHackathon(LocalDate.now().minusDays(1)
                ,new Period(LocalDate.now(),LocalDate.now().plusDays(1)),
                4,new HackathonStatus(HackathonStatus.HackathonStateType.PROGRESS));
        User user = createUser("Gino","gino@gino.it");
        Team team = createTestTeam("IGiniss",user);

        SubmissionRequest sub = new SubmissionRequest("https://ciao");

        mockMvc.perform(post(path+hackathon.getId())
                        .header("Authorization","Bearer "+getToken(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sub)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.url").value("Invalid URL format"));

        Hackathon a = hackathonRepository.findById(hackathon.getId()).get();
        assertThat(a.getSubmission(team.getName())).isNull();
    }

    @Test
    void addSubmission_SubmissionClosed_ShouldReturn400() throws Exception {
        Hackathon hackathon = createTestHackathon(LocalDate.now().minusDays(3)
                ,new Period(LocalDate.now().minusDays(2),LocalDate.now().minusDays(1)),
                4,new HackathonStatus(HackathonStatus.HackathonStateType.EVALUATION));
        User user = createUser("Gino","gino@gino.it");
        Team team = createTestTeam("IGiniss",user);

        SubmissionRequest sub = new SubmissionRequest("https://ciao.site");

        mockMvc.perform(post(path+hackathon.getId())
                        .header("Authorization","Bearer "+getToken(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sub)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Can't add submission in this state"));

        Hackathon a = hackathonRepository.findById(hackathon.getId()).get();
        assertThat(a.getSubmission(team.getName())).isNull();
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
