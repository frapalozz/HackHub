package unicam.hackhub.team;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import unicam.hackhub.config.DataInitializer;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;
import unicam.hackhub.presentation.dto.request.TeamRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class CreateTeamIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @MockitoBean
    DataInitializer dataInitializer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createTeam_endToEnd_shouldPersistTeamAndReturn201() throws Exception {
        User user = new User("integrazione@test.it", "Mario");
        userRepository.save(user);

        TeamRequest request = new TeamRequest(user.getEmail(), "TeamIntegrazione", List.of());

        mockMvc.perform(post("/api/v1/user/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Team Created"));

        Team savedTeam = teamRepository.findById("TeamIntegrazione").orElse(null);
        assertThat(savedTeam).isNotNull();
        assertThat(savedTeam.getMembers()).contains(user);

        User updatedUser = userRepository.findById(user.getEmail()).orElse(null);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.hasTeam()).isTrue();
        assertThat(updatedUser.getTeam().getName()).isEqualTo("TeamIntegrazione");
    }

    @Test
    void createTeam_success_shouldReturn201AndPersistTeam() throws Exception {
        // ARRANGE: utente senza team
        User user = new User("success@test.it", "Luigi");
        userRepository.save(user);

        TeamRequest request = new TeamRequest(user.getEmail(), "teamFantastico", List.of());

        // ACT
        mockMvc.perform(post("/api/v1/user/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Team Created"));

        // VERIFICA: team salvato e utente associato
        Team savedTeam = teamRepository.findById("teamFantastico").orElse(null);
        assertThat(savedTeam).isNotNull();
        assertThat(savedTeam.getMembers()).contains(user);

        User updatedUser = userRepository.findById(user.getEmail()).orElseThrow();
        assertThat(updatedUser.hasTeam()).isTrue();
        assertThat(updatedUser.getTeam().getName()).isEqualTo("teamFantastico");
    }

    @Test
    void createTeam_teamNameAlreadyUsed_shouldReturn400() throws Exception {
        // ARRANGE: crea un team con nome "teamOccupato"
        User owner = new User("owner@test.it", "Owner");
        Team occupiedTeam = new Team("teamOccupato", owner);
        owner.setTeam(occupiedTeam);
        teamRepository.save(occupiedTeam);
        userRepository.save(owner);

        // Un secondo utente tenta di creare un team con lo stesso nome
        User anotherUser = new User("another@test.it", "Another");
        userRepository.save(anotherUser);

        TeamRequest request = new TeamRequest(anotherUser.getEmail(), "teamOccupato", List.of());

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/user/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Team name already used"))
                .andExpect(jsonPath("$.status").value("400"));

        // VERIFICA: il secondo utente NON ha team
        User updatedAnother = userRepository.findById(anotherUser.getEmail()).orElseThrow();
        assertThat(updatedAnother.hasTeam()).isFalse();
        // Il team originale è ancora composto solo dal proprietario
        Team team = teamRepository.findById("teamOccupato").orElseThrow();
        assertThat(team.getMembers()).containsExactly(owner);
    }

    @Test
    void createTeam_userNotFound_shouldReturn404() throws Exception {
        // ARRANGE: nessun utente con quell'email
        TeamRequest request = new TeamRequest("inesistente@test.it", "teamQualsiasi", List.of());

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/user/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.status").value(404));


        // VERIFICA: nessun team creato
        assertThat(teamRepository.findById("teamQualsiasi")).isEmpty();
    }

    @Test
    void createTeam_userAlreadyInTeam_shouldReturn409() throws Exception {
        // ARRANGE: crea utente con un team esistente
        User user = new User("user@test.it", "Mario");
        Team existingTeam = new Team("teamEsistente", user);
        user.setTeam(existingTeam);
        teamRepository.save(existingTeam);
        userRepository.save(user);

        TeamRequest request = new TeamRequest(user.getEmail(), "nuovoTeam", List.of());

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/user/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())   // IllegalStateException → 409
                .andExpect(jsonPath("$.message").value("User already in a team"));

        // VERIFICA: il nuovo team NON è stato creato
        assertThat(teamRepository.findById("nuovoTeam")).isEmpty();
        // L'utente è ancora nel vecchio team
        User updatedUser = userRepository.findById(user.getEmail()).orElseThrow();
        assertThat(updatedUser.getTeam().getName()).isEqualTo("teamEsistente");
    }
}
