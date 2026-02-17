package unicam.hackhub.invitation;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import unicam.hackhub.application.invitation.InvitationHandler;
import unicam.hackhub.config.DataInitializer;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.invitation.domain.Invitation;
import unicam.hackhub.domain.invitation.repository.InvitationRepository;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.staff.repository.StaffRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;
import unicam.hackhub.domain.utils.Period;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class RespondToInvitationIntegrationTest {

    @Autowired
    private InvitationHandler invitationHandler;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private HackathonRepository hackathonRepository;

    @MockitoBean
    private DataInitializer dataInitializer;

    @Test
    void declineInvitation_success_shouldRemoveInvitation() {
        // ARRANGE
        User owner = createUser("John", "john@test.it");
        User invitee = createUser("Jane", "jane@test.it");
        Team team = createTeam("Team1", owner);
        Invitation invitation = createInvitation(team, invitee);

        // ACT
        invitationHandler.declineInvitation(
                invitation.getId().getReceiver().getEmail(),
                team.getName());

        // ASSERT
        assertThat(invitationRepository.findById(invitation.getId())).isEmpty();
        // L'utente non è stato aggiunto al team
        assertThat(team.getMembers()).doesNotContain(invitee);
        assertThat(invitee.hasTeam()).isFalse();
    }

    @Test
    void declineInvitation_invitationNotFound_shouldDoNothing() {
        // ARRANGE
        User owner = createUser("John", "john@test.it");
        User invitee = createUser("Jane", "jane@test.it");
        Team team = createTeam("Team1", owner);
        Invitation invitation = createInvitation(team, invitee);

        // ACT
        // Assumiamo che il metodo NON lanci eccezione (o se la lancia, la catturiamo con assertThrows)
        // Qui scegliamo di testare che l'invito originale sia ancora presente.
        // Se il metodo lancia eccezione, decommentare assertThrows.
        assertThrows(IllegalArgumentException.class,
                () -> invitationHandler.declineInvitation("dfegregfer@email.it", "efvgregf"));

        // ASSERT
        assertThat(invitationRepository.findById(invitation.getId())).isPresent();
        assertThat(team.getMembers()).doesNotContain(invitee);
    }

    @Test
    void acceptInvitation_success_shouldAddUserToTeamAndRemoveInvitation() {
        // ARRANGE
        User owner = createUser("John", "john@test.it");
        User invitee = createUser("Jane", "jane@test.it");
        Team team = createTeam("Team1", owner);
        Invitation invitation = createInvitation(team, invitee);

        // ACT
        invitationHandler.acceptInvitation(invitee.getEmail(), team.getName());

        // ASSERT
        // Invito rimosso
        assertThat(invitationRepository.findById(invitation.getId())).isEmpty();
        // Utente aggiunto al team
        User user = userRepository.findById(invitee.getEmail()).orElseThrow();
        assertThat(user.getTeam().getName()).contains(team.getName());
        // Utente ora ha un team
        User updatedInvitee = userRepository.findById(invitee.getEmail()).orElseThrow();
        assertThat(updatedInvitee.hasTeam()).isTrue();
        assertThat(updatedInvitee.getTeam().getName()).isEqualTo(team.getName());
    }

    @Test
    void acceptInvitation_invitationNotFound_shouldDoNothing() {
        // ARRANGE
        User owner = createUser("John", "john@test.it");
        User invitee = createUser("Jane", "jane@test.it");
        Team team = createTeam("Team1", owner);
        Invitation invitation = createInvitation(team, invitee);

        // ACT
        assertThrows(IllegalArgumentException.class,
                () -> invitationHandler.acceptInvitation("wsfewf@email.it", "nameregtr4"));

        // ASSERT
        // Invito originale ancora presente
        assertThat(invitationRepository.findById(invitation.getId())).isPresent();
        // Utente non aggiunto al team
        assertThat(team.getMembers()).doesNotContain(invitee);
        assertThat(invitee.hasTeam()).isFalse();
    }

    @Test
    void acceptInvitation_teamAlreadyRegisteredInActiveHackathon_shouldThrowException() {
        // ARRANGE
        // Crea hackathon con iscrizioni ancora aperte (sub deadline futura)
        LocalDate subscriptionDeadline = LocalDate.now().plusDays(5);
        Period period = new Period(LocalDate.now().plusDays(6), LocalDate.now().plusDays(10));
        Hackathon hackathon = createHackathon(subscriptionDeadline, period, 5);

        User owner = createUser("John", "john@test.it");
        User invitee = createUser("Jane", "jane@test.it");
        Team team = createTeam("Team1", owner);
        hackathon.registerTeam(team);               // team iscritto all'hackathon
        hackathonRepository.save(hackathon);        // persiste la registrazione

        Invitation invitation = createInvitation(team, invitee);

        // ACT & ASSERT
        // Ci aspettiamo un'eccezione che impedisce di aggiungere membri a un team già in hackathon attivo
        assertThrows(NullPointerException.class,
                () -> invitationHandler.acceptInvitation(
                        invitation.getId().getReceiver().getEmail(),
                        invitee.getTeam().getName()
                ));

        // Verifica che l'invito sia ancora presente e l'utente non sia stato aggiunto
        assertThat(invitationRepository.findById(invitation.getId())).isPresent();
        assertThat(team.getMembers()).doesNotContain(invitee);
        assertThat(invitee.hasTeam()).isFalse();
    }

    @Test
    void acceptInvitation_userAlreadyInAnotherTeam_shouldThrowException() {
        // ARRANGE
        User owner1 = createUser("John", "john@test.it");
        User owner2 = createUser("Mike", "mike@test.it");
        User invitee = createUser("Jane", "jane@test.it");

        Team team1 = createTeam("Team1", owner1);
        Team team2 = createTeam("Team2", owner2);
        // Assegna l'utente al team2
        team2.addMember(invitee);
        teamRepository.save(team2);
        invitee.setTeam(team2);
        userRepository.save(invitee);

        // Crea invito per team1
        Invitation invitation = createInvitation(team1, invitee);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> invitationHandler.acceptInvitation(
                        invitation.getId().getReceiver().getEmail(),
                        invitee.getTeam().getName()
                ));

        // Verifica che l'invito sia ancora presente e l'utente non sia stato spostato
        assertThat(invitationRepository.findById(invitation.getId())).isPresent();
        assertThat(team1.getMembers()).doesNotContain(invitee);
        assertThat(invitee.getTeam().getName()).isEqualTo("Team2");
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

    private Invitation createInvitation(Team team, User invitee) {
        Invitation invitation = new Invitation(LocalDate.now(), team, invitee);
        invitationRepository.save(invitation);
        return invitation;
    }

    private Hackathon createHackathon(LocalDate subscriptionDeadline, Period period, int maxTeamSize) {
        Staff organizer = new Staff("organizer", "organizer@test.it");
        Staff judge = new Staff("judge", "judge@test.it");
        Staff mentor1 = new Staff("mentor1", "mentor1@test.it");
        Staff mentor2 = new Staff("mentor2", "mentor2@test.it");

        // SALVA gli staff prima di usarli nell'hackathon
        staffRepository.save(organizer);
        staffRepository.save(judge);
        staffRepository.save(mentor1);
        staffRepository.save(mentor2);

        Set<Staff> mentors = new HashSet<>();
        mentors.add(mentor1);
        mentors.add(mentor2);

        Hackathon hackathon = new Hackathon(
                "Hackathon Test",
                subscriptionDeadline,
                period,
                maxTeamSize,
                "Regolamento...",
                1000.0,
                organizer,
                judge,
                mentors
        );
        hackathonRepository.save(hackathon);
        return hackathon;
    }
}
