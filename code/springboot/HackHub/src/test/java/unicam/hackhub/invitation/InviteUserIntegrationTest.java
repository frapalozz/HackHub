package unicam.hackhub.invitation;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import unicam.hackhub.application.invitation.InvitationHandler;
import unicam.hackhub.config.DataInitializer;
import unicam.hackhub.domain.invitation.domain.InvitationId;
import unicam.hackhub.domain.invitation.repository.InvitationRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class InviteUserIntegrationTest {

    @Autowired
    private InvitationHandler invitationHandler;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @MockitoBean
    private DataInitializer dataInitializer;

    @Test
    void inviteUser_success_shouldCreateInvitation() {
        // ARRANGE
        Team team = createTeamWithOwner("Team1", "owner@test.it", "Owner");
        User invitee = new User("Jane", "jane@test.it");
        userRepository.save(invitee);

        // ACT
        invitationHandler.inviteUser(invitee.getEmail(), team.getName());

        // ASSERT
        InvitationId id = new InvitationId(team, invitee);
        assertThat(invitationRepository.existsById(id)).isTrue();
    }

    @Test
    void inviteUser_invalidEmail_shouldNotCreateInvitation() {
        // ARRANGE
        Team team = createTeamWithOwner("Team1", "owner@test.it", "Owner");
        User invitee = new User("Jane", "invalid-email");   // email non valida
        userRepository.save(invitee);

        // ACT
        // assumendo che l'handler NON lanci eccezione ma semplicemente non crei l'invito
        assertThrows(IllegalArgumentException.class, () -> invitationHandler.inviteUser(invitee.getEmail(), team.getName()));

        // ASSERT
        InvitationId id = new InvitationId(team, invitee);
        assertThat(invitationRepository.existsById(id)).isFalse();
    }

    @Test
    void inviteUser_userNotFound_shouldNotCreateInvitation() {
        // ARRANGE
        Team team = createTeamWithOwner("Team1", "owner@test.it", "Owner");

        // ACT
        assertThrows(IllegalArgumentException.class,
                () -> invitationHandler.inviteUser("nonexistent@test.it", team.getName()));

        // ASSERT
        // non possiamo creare InvitationId perché l'utente non esiste; verifichiamo che non ci siano inviti per questo team
        assertThat(invitationRepository.findAll()).isEmpty();
    }

    @Test
    void inviteUser_invitationAlreadyExists_shouldNotCreateDuplicate() {
        // ARRANGE
        Team team = createTeamWithOwner("Team1", "owner@test.it", "Owner");
        User invitee = new User("Jane", "jane@test.it");
        userRepository.save(invitee);

        // ACT
        invitationHandler.inviteUser(invitee.getEmail(), team.getName());   // primo invito
        assertThrows(IllegalArgumentException.class,
                () -> invitationHandler.inviteUser(invitee.getEmail(), team.getName()));   // secondo invito

        // ASSERT
        InvitationId id = new InvitationId(team, invitee);
        assertThat(invitationRepository.existsById(id)).isTrue();
    }

    private Team createTeamWithOwner(String teamName, String ownerEmail, String ownerName) {
        User owner = new User(ownerName, ownerEmail);
        userRepository.save(owner);
        Team team = new Team(teamName, owner);
        teamRepository.save(team);
        return team;
    }
}
