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
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.staff.repository.StaffRepository;
import unicam.hackhub.domain.utils.Role;
import unicam.hackhub.infrastructure.security.JwtTokenUtil;
import unicam.hackhub.presentation.dto.request.HackathonRequest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class CreateHackathonTests {

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

    private final String path = "/api/v1/staff/hackathon";

    @Test
    void createHackathon_Success_ShouldReturn201() throws Exception {
        // ARRANGE
        Staff organizer = createStaff("organizer", "organizer@organizer.it");
        Staff judge = createStaff("judge", "judge@judge.it");
        Staff mentor1 = createStaff("mentor1", "mentor1@mentor1.it");
        Staff mentor2 = createStaff("mentor2", "mentor2@mentor2.it");

        HackathonRequest request = new HackathonRequest(
                "NuovoHackathon",
                "Remoto",
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4),
                LocalDate.now().plusDays(5),
                5,
                "Regole Hackathon",
                500.0,
                judge.getEmail(),
                List.of(mentor1.getEmail(), mentor2.getEmail())
        );

        // ACT
        mockMvc.perform(post(path)
                    .header("Authorization","Bearer "+getToken(organizer))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Hackathon " + "NuovoHackathon" + " created"));

        // ASSERT
        assertThat(hackathonRepository.findAll()).isNotEmpty();
        assertThat(hackathonRepository.findAll().getFirst().getOrganizer().getEmail()).isEqualTo(organizer.getEmail());
    }

    @Test
    void createHackathon_InvalidDateOrder_ShouldReturn409() throws Exception {
        // ARRANGE
        Staff organizer = createStaff("organizer", "organizer@organizer.it");
        Staff judge = createStaff("judge", "judge@judge.it");
        Staff mentor1 = createStaff("mentor1", "mentor1@mentor1.it");
        Staff mentor2 = createStaff("mentor2", "mentor2@mentor2.it");

        HackathonRequest request = new HackathonRequest(
                "NuovoHackathon",
                "Remoto",
                LocalDate.now().plusDays(8),
                LocalDate.now().plusDays(7),
                LocalDate.now().plusDays(5),
                5,
                "Regole Hackathon",
                500.0,
                judge.getEmail(),
                List.of(mentor1.getEmail(), mentor2.getEmail())
        );

        // ACT
        mockMvc.perform(post(path)
                        .header("Authorization","Bearer "+getToken(organizer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Dates order not valid"));

        // ASSERT
        assertThat(hackathonRepository.findAll()).isEmpty();
    }

    @Test
    void createHackathon_StaffNotFound_ShouldReturn404() throws Exception {
        // ARRANGE
        Staff organizer = createStaff("organizer", "organizer@organizer.it");
        Staff judge = createStaff("judge", "judge@judge.it");
        Staff mentor1 = createStaff("mentor1", "mentor1@mentor1.it");
        Staff mentor2 = createStaff("mentor2", "mentor2@mentor2.it");

        HackathonRequest request = new HackathonRequest(
                "NuovoHackathon",
                "Remoto",
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4),
                LocalDate.now().plusDays(5),
                5,
                "Regole Hackathon",
                500.0,
                judge.getEmail(),
                List.of(mentor1.getEmail()+"ff", mentor2.getEmail())
        );

        // ACT
        mockMvc.perform(post(path)
                        .header("Authorization","Bearer "+getToken(organizer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Staff not found"));

        // ASSERT
        assertThat(hackathonRepository.findAll()).isEmpty();
    }

    @Test
    void createHackathon_InvalidPrize_ShouldReturn201() throws Exception {
        // ARRANGE
        Staff organizer = createStaff("organizer", "organizer@organizer.it");
        Staff judge = createStaff("judge", "judge@judge.it");
        Staff mentor1 = createStaff("mentor1", "mentor1@mentor1.it");
        Staff mentor2 = createStaff("mentor2", "mentor2@mentor2.it");

        HackathonRequest request = new HackathonRequest(
                "NuovoHackathon",
                "Remoto",
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4),
                LocalDate.now().plusDays(5),
                5,
                "Regole Hackathon",
                -30.0,
                judge.getEmail(),
                List.of(mentor1.getEmail(), mentor2.getEmail())
        );

        // ACT
        mockMvc.perform(post(path)
                        .header("Authorization","Bearer "+getToken(organizer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.prize").value("Prize deve essere zero o positivo"));

        // ASSERT
        assertThat(hackathonRepository.findAll()).isEmpty();
    }

    @Test
    void createHackathon_StaffSelf_ShouldReturn409() throws Exception {
        // ARRANGE
        Staff organizer = createStaff("organizer", "organizer@organizer.it");
        Staff mentor1 = createStaff("mentor1", "mentor1@mentor1.it");
        Staff mentor2 = createStaff("mentor2", "mentor2@mentor2.it");

        HackathonRequest request = new HackathonRequest(
                "NuovoHackathon",
                "Remoto",
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4),
                LocalDate.now().plusDays(5),
                5,
                "Regole Hackathon",
                500.0,
                organizer.getEmail(),
                List.of(mentor1.getEmail(), mentor2.getEmail())
        );

        // ACT
        mockMvc.perform(post(path)
                        .header("Authorization","Bearer "+getToken(organizer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Staff cannot be self"));

        // ASSERT
        assertThat(hackathonRepository.findAll()).isEmpty();
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
