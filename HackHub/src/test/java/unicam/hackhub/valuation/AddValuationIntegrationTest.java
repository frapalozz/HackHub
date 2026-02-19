package unicam.hackhub.valuation;

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
import unicam.hackhub.domain.hackathon.model.Submission;
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
import unicam.hackhub.presentation.dto.request.ValuateSubmissionRequest;

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
public class AddValuationIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @MockitoBean
    DataInitializer dataInitializer;
    private final String path = "/api/v1/staff/hackathon/";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private HackathonRepository hackathonRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addValuation_Success_ShouldReturn200() throws Exception {

        // ARRANGE
        Staff judge = staffRepository.save(new Staff("judge", "judge@test.it", "password"));
        Team team = createTestTeam("IGiniss",createUser("Gino","gino@gino.it"));
        Hackathon hackathon = createTestHackathon(LocalDate.now().minusDays(3)
                ,new Period(LocalDate.now().minusDays(2),LocalDate.now().minusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.EVALUATION), team, new Submission("test.url"),
                judge);

        ValuateSubmissionRequest request = new ValuateSubmissionRequest(5, "good submission");

        // ACT
        mockMvc.perform(post(path + hackathon.getId() + "/" + team.getName())
                        .header("Authorization","Bearer "+getToken(judge))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Valuation added"));

        // ASSERT
        Submission submission = hackathonRepository.findById(hackathon.getId())
                .get().getSubmission(team);
        assertThat(submission.getValuation()).isNotNull();
    }

    @Test
    void addValuation_AlreadyPresent_ShouldReturn409() throws Exception {

        // ARRANGE
        Staff judge = staffRepository.save(new Staff("judge", "judge@test.it", "password"));
        Team team = createTestTeam("IGiniss",createUser("Gino","gino@gino.it"));
        Hackathon hackathon = createTestHackathon(LocalDate.now().minusDays(3)
                ,new Period(LocalDate.now().minusDays(2),LocalDate.now().minusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.EVALUATION), team, new Submission("test.url"),
                judge);

        ValuateSubmissionRequest request = new ValuateSubmissionRequest(5, "good submission");

        // ACT
        mockMvc.perform(post(path + hackathon.getId() + "/" + team.getName())
                        .header("Authorization","Bearer "+getToken(judge))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Valuation added"));

        // ASSERT
        Submission submission = hackathonRepository.findById(hackathon.getId())
                .get().getSubmission(team);
        assertThat(submission.getValuation()).isNotNull();

        request = new ValuateSubmissionRequest(3, "good submission");

        mockMvc.perform(post(path + hackathon.getId() + "/" + team.getName())
                        .header("Authorization","Bearer "+getToken(judge))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Valuation already exists"));

        submission = submissionRepository.findById(1L).get();
        assertThat(submission.getValuation().getVote()).isEqualTo(5);
    }

    @Test
    void addValuation_HackathonNotFound_ShouldReturn404() throws Exception {

        // ARRANGE
        Staff judge = staffRepository.save(new Staff("judge", "judge@test.it", "password"));
        Team team = createTestTeam("IGiniss",createUser("Gino","gino@gino.it"));
        Hackathon hackathon = createTestHackathon(LocalDate.now().minusDays(3)
                ,new Period(LocalDate.now().minusDays(2),LocalDate.now().minusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.EVALUATION), team, new Submission("test.url"),
                judge);

        ValuateSubmissionRequest request = new ValuateSubmissionRequest(5, "good submission");

        // ACT
        mockMvc.perform(post(path + (hackathon.getId()-1) + "/" + team.getName())
                        .header("Authorization","Bearer "+getToken(judge))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Hackathon not found"));
    }

    @Test
    void addValuation_SubmissionNotFound_ShouldReturn404() throws Exception {

        // ARRANGE
        Staff judge = staffRepository.save(new Staff("judge", "judge@test.it", "password"));
        Team team = createTestTeam("IGiniss",createUser("Gino","gino@gino.it"));
        Hackathon hackathon = createTestHackathon(LocalDate.now().minusDays(3)
                ,new Period(LocalDate.now().minusDays(2),LocalDate.now().minusDays(1)),
                new HackathonStatus(HackathonStatus.HackathonStateType.EVALUATION), team, new Submission("test.url"),
                judge);

        ValuateSubmissionRequest request = new ValuateSubmissionRequest(5, "good submission");

        // ACT
        mockMvc.perform(post(path + hackathon.getId() + "/" + team.getName()+"ssd")
                        .header("Authorization","Bearer "+getToken(judge))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Submission not found"));
    }

    private Hackathon createTestHackathon(LocalDate subscriptionDeadline, Period period, HackathonStatus state,
                                          Team team, Submission sub, Staff judge) {
        Staff organizer = new Staff("organizer", "organizer@test.it", "password");
        Staff mentor1 = new Staff("mentor1", "mentor1@test.it", "password");
        Staff mentor2 = new Staff("mentor2", "mentor2@test.it", "password");

        // SALVA gli staff prima di usarli nell'hackathon
        staffRepository.save(organizer);
        staffRepository.save(mentor1);
        staffRepository.save(mentor2);

        Set<Staff> mentors = new HashSet<>();
        mentors.add(mentor1);
        mentors.add(mentor2);

        Map<Team, Submission> submissions = new HashMap<>();
        if(sub != null) {
            submissions.put(team, sub);
        }


        Hackathon hackathon = Hackathon.builder()
                .name("Hackathon Test")
                .subscriptionDeadline(subscriptionDeadline)
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

    private String getToken(Staff user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        return jwtTokenUtil.generateToken(userDetails);
    }
}
