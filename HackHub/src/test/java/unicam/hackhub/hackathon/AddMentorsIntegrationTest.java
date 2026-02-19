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
import tools.jackson.databind.ObjectMapper;
import unicam.hackhub.config.DataInitializer;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.state.HackathonStatus;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.staff.repository.StaffRepository;
import unicam.hackhub.domain.utils.Period;
import unicam.hackhub.domain.utils.Role;
import unicam.hackhub.infrastructure.security.JwtTokenUtil;
import unicam.hackhub.presentation.dto.request.AddMentorsRequest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class AddMentorsIntegrationTest {

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private HackathonRepository hackathonRepository;
    @MockitoBean
    private DataInitializer dataInitializer;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String path = "/api/v1/staff/hackathon/";

    @Test
    void addMentors_Success_ShouldReturn200() throws Exception {
        // ARRANGE
        Staff organizer = createStaff("organizer", "organizer@test.it");
        Staff mentor3 = createStaff("mentor3", "mentor3@test.it");
        Staff mentor4 = createStaff("mentor4", "mentor4@test.it");
        Hackathon hackathon = createTestHackathon(
                new HackathonStatus(HackathonStatus.HackathonStateType.SUBSCRIPTION),
                organizer
        );

        AddMentorsRequest request = new AddMentorsRequest(
                List.of(mentor3.getEmail(), mentor4.getEmail())
        );

        // ACT
        mockMvc.perform(put(path+hackathon.getId()+"/addMentors")
                    .header("Authorization","Bearer "+getToken(organizer))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Mentors added"));

        // ASSERT
        Hackathon hack = hackathonRepository.findById(hackathon.getId())
                .orElseThrow(IllegalArgumentException::new);
        assertThat(hack.getMentors().containsAll(List.of(mentor3, mentor4))).isTrue();
    }

    @Test
    void addMentors_MentorsNotFound_ShouldReturn404() throws Exception {
        // ARRANGE
        Staff organizer = createStaff("organizer", "organizer@test.it");
        Staff mentor3 = createStaff("mentor3", "mentor3@test.it");
        Staff mentor4 = createStaff("mentor4", "mentor4@test.it");
        Hackathon hackathon = createTestHackathon(
                new HackathonStatus(HackathonStatus.HackathonStateType.SUBSCRIPTION),
                organizer
        );

        AddMentorsRequest request = new AddMentorsRequest(
                List.of(mentor3.getEmail()+"gs", mentor4.getEmail())
        );

        // ACT
        mockMvc.perform(put(path+hackathon.getId()+"/addMentors")
                        .header("Authorization","Bearer "+getToken(organizer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Mentors not found"));

        // ASSERT
        Hackathon hack = hackathonRepository.findById(hackathon.getId())
                .orElseThrow(IllegalArgumentException::new);
        assertThat(hack.getMentors().containsAll(List.of(mentor3, mentor4))).isFalse();
    }

    @Test
    void addMentors_CannotAddSelf_ShouldReturn409() throws Exception {
        // ARRANGE
        Staff organizer = createStaff("organizer", "organizer@test.it");
        Staff mentor3 = createStaff("mentor3", "mentor3@test.it");
        Staff mentor4 = createStaff("mentor4", "mentor4@test.it");
        Hackathon hackathon = createTestHackathon(
                new HackathonStatus(HackathonStatus.HackathonStateType.SUBSCRIPTION),
                organizer
        );

        AddMentorsRequest request = new AddMentorsRequest(
                List.of(mentor3.getEmail(), mentor4.getEmail(), organizer.getEmail())
        );

        // ACT
        mockMvc.perform(put(path+hackathon.getId()+"/addMentors")
                        .header("Authorization","Bearer "+getToken(organizer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Staff already exists in hackathon"));

        // ASSERT
        Hackathon hack = hackathonRepository.findById(hackathon.getId())
                .orElseThrow(IllegalArgumentException::new);
        assertThat(hack.getMentors().containsAll(List.of(mentor3, mentor4, organizer))).isFalse();
    }



    private Hackathon createTestHackathon(HackathonStatus state, Staff organizer) {
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
                .subscriptionDeadline(LocalDate.now())
                .hackathonPeriod(new Period(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)))
                .maxTeamSize(4)
                .requirements("Regolamento...")
                .prize(1000.0)
                .organizer(organizer)
                .judge(judge)
                .mentors(mentors)
                .status(state)
                .build();

        return hackathonRepository.save(hackathon);
    }

    private Staff createStaff(String name, String email) {
        Staff staff = new Staff(name, email, "password");
        staff.setRole(Role.STAFF);
        return staffRepository.save(staff);
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
