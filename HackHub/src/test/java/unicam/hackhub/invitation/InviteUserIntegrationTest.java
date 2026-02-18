package unicam.hackhub.invitation;

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
import unicam.hackhub.domain.invitation.domain.InvitationId;
import unicam.hackhub.domain.invitation.repository.InvitationRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;
import unicam.hackhub.domain.utils.Role;
import unicam.hackhub.infrastructure.security.JwtTokenUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class InviteUserIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private InvitationRepository invitationRepository;
    @MockitoBean
    private DataInitializer dataInitializer;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private final String path = "/api/v1/team/invite?userEmail=";

    @Test
    void inviteUser_success_shouldCreateInvitation() throws Exception {
        // ARRANGE
        User owner = createUser("owner", "owner@test.it", Role.TEAM_MEMBER);
        Team team = createTeam("team1", owner);
        User invitee = createUser("Jane", "jane@test.it");

        // ACT
        mockMvc.perform(post(path + "jane@test.it")
                        .header("Authorization", "Bearer " + getToken(owner))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("Invitation saved"));

        // ASSERT
        InvitationId id = new InvitationId(team, invitee);
        assertThat(invitationRepository.existsById(id)).isTrue();
    }

    @Test
    void inviteUser_invalidEmail_shouldNotCreateInvitation() throws Exception {
        // ARRANGE
        User owner = createUser("owner", "owner@test.it", Role.TEAM_MEMBER);
        Team team = createTeam("team1", owner);
        User invitee = createUser("Jane", "invalid-email");
        userRepository.save(invitee);

        // ACT
        mockMvc.perform(post(path + "jane@test")
                        .header("Authorization", "Bearer " + getToken(owner))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid email address"));

        // ASSERT
        InvitationId id = new InvitationId(team, invitee);
        assertThat(invitationRepository.existsById(id)).isFalse();
    }

    @Test
    void inviteUser_userNotFound_shouldNotCreateInvitation() throws Exception {
        // ARRANGE
        User owner = createUser("owner", "owner@test.it", Role.TEAM_MEMBER);
        Team team = createTeam("team1", owner);

        // ACT
        mockMvc.perform(post(path + "jane@test.it")
                        .header("Authorization", "Bearer " + getToken(owner))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));

        // ASSERT
        // non possiamo creare InvitationId perché l'utente non esiste; verifichiamo che non ci siano inviti per questo team
        assertThat(invitationRepository.findAll()).isEmpty();
    }

    @Test
    void inviteUser_invitationAlreadyExists_shouldNotCreateDuplicate() throws Exception {
        // ARRANGE
        User owner = createUser("owner", "owner@test.it", Role.TEAM_MEMBER);
        Team team = createTeam("team1", owner);
        User invitee = createUser("Jane", "jane@test.it");
        userRepository.save(invitee);

        // ACT
        // primo invito
        mockMvc.perform(post(path + "jane@test.it")
                        .header("Authorization", "Bearer " + getToken(owner))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("Invitation saved"));
        // secondo invito
        mockMvc.perform(post(path + "jane@test.it")
                        .header("Authorization", "Bearer " + getToken(owner))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Invitation already exists"));

        // ASSERT
        InvitationId id = new InvitationId(team, invitee);
        assertThat(invitationRepository.existsById(id)).isTrue();
    }

    @Test
    void inviteSelf_shouldNotCreateInvitation() throws Exception {
        // ARRANGE
        User owner = createUser("owner", "owner@test.it", Role.TEAM_MEMBER);
        Team team = createTeam("team1", owner);

        // ACT
        mockMvc.perform(post(path + "owner@test.it")
                        .header("Authorization", "Bearer " + getToken(owner))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string("Cannot invite self"));

        // ASSERT
        InvitationId id = new InvitationId(team, owner);
        assertThat(invitationRepository.existsById(id)).isFalse();
    }

    private User createUser(String name, String email) {
        return userRepository.save(new User(name, email, "password"));
    }

    private User createUser(String name, String email, Role role) {
        User user = new User(name, email, "password");
        user.setRole(role);
        return userRepository.save(user);
    }

    private Team createTeam(String teamName, User owner) {
        Team team = teamRepository.save(new Team(teamName, owner));
        owner.setTeam(team);
        userRepository.save(owner);
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
