package unicam.hackhub.invitation;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import unicam.hackhub.application.invitation.InvitationHandler;
import unicam.hackhub.application.dto.response.InvitationResponse;
import unicam.hackhub.config.DataInitializer;
import unicam.hackhub.domain.invitation.domain.Invitation;
import unicam.hackhub.domain.invitation.repository.InvitationRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class QueryInvitationsIntegrationTest {

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
    void getInvitations_zeroInvitations_shouldReturnEmptyList() {
        // ARRANGE
        User user = createUser("John", "john@test.it");

        // ACT
        List<InvitationResponse> invitations = invitationHandler.getInvitations(user.getEmail());

        // ASSERT
        assertThat(invitations).isEmpty();
    }

    @Test
    void getInvitations_oneInvitation_shouldReturnListWithOneElement() {
        // ARRANGE
        User owner = createUser("Owner", "owner@test.it");
        User invitee = createUser("Jane", "jane@test.it");

        Team team = createTeam("Team1", owner);

        Invitation invitation = new Invitation(LocalDate.now(), team, invitee);
        invitationRepository.save(invitation);

        // ACT
        List<InvitationResponse> invitations = invitationHandler.getInvitations(invitee.getEmail());

        // ASSERT
        assertThat(invitations).hasSize(1);
        assertThat(invitations.getFirst().teamName()).isEqualTo("Team1");
        assertThat(invitations.getFirst().receiver().getEmail()).isEqualTo(invitee.getEmail());
    }

    @Test
    void getInvitations_twoInvitations_shouldReturnListWithTwoElements() {
        // ARRANGE
        User owner1 = createUser("Owner1", "owner1@test.it");
        User owner2 = createUser("Owner2", "owner2@test.it");
        User invitee = createUser("Jane", "jane@test.it");

        Team team1 = createTeam("Team1", owner1);
        Team team2 = createTeam("Team2", owner2);

        Invitation invitation1 = new Invitation(LocalDate.now(), team1, invitee);
        Invitation invitation2 = new Invitation(LocalDate.now(), team2, invitee);
        invitationRepository.save(invitation1);
        invitationRepository.save(invitation2);

        // ACT
        List<InvitationResponse> invitations = invitationHandler.getInvitations(invitee.getEmail());

        // ASSERT
        assertThat(invitations).hasSize(2);
        assertThat(invitations)
                .extracting(InvitationResponse::teamName)
                .containsExactlyInAnyOrder("Team1", "Team2");
    }

    private User createUser(String name, String email) {
        User user = new User(name, email);
        userRepository.save(user);
        return user;
    }

    private Team createTeam(String teamName, User owner) {
        Team team = new Team(teamName, owner);
        teamRepository.save(team);
        return team;
    }
}
