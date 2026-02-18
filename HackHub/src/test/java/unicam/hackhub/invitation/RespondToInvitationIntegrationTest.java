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
import tools.jackson.databind.ObjectMapper;
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
import unicam.hackhub.infrastructure.security.JwtTokenUtil;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class RespondToInvitationIntegrationTest {

    @Autowired
    MockMvc mockMvc;
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
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @MockitoBean
    private DataInitializer dataInitializer;
    private final String path = "/api/v1/user/invitation";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void declineInvitation_success_shouldRemoveInvitation() throws Exception {
        // ARRANGE
        User owner = createUser("John", "john@test.it");
        User invitee = createUser("Jane", "jane@test.it");
        Team team = createTeam("Team1", owner);
        Invitation invitation = createInvitation(team, invitee);

        // ACT
        mockMvc.perform(delete(path + "/Team1")
                            .header("Authorization", "Bearer " + getToken(invitee))
                            .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Invitation deleted"));

        // ASSERT
        assertThat(invitationRepository.findById(invitation.getId())).isEmpty();
        // L'utente non è stato aggiunto al team
        assertThat(team.getMembers()).doesNotContain(invitee);
        assertThat(invitee.hasTeam()).isFalse();
    }

    @Test
    void declineInvitation_invitationNotFound_shouldDoNothing() throws Exception {
        // ARRANGE
        User owner = createUser("John", "john@test.it");
        User invitee = createUser("Jane", "jane@test.it");
        Team team = createTeam("Team1", owner);

        // ACT
        mockMvc.perform(delete(path + "/Team1")
                        .header("Authorization", "Bearer " + getToken(invitee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Invitation not found"));

        assertThat(invitee.getTeam()).isNull();
    }

    @Test
    void acceptInvitation_success_shouldAddUserToTeamAndRemoveInvitation() throws Exception {
        // ARRANGE
        User owner = createUser("John", "john@test.it");
        User invitee = createUser("Jane", "jane@test.it");
        Team team = createTeam("Team1", owner);
        Invitation invitation = createInvitation(team, invitee);

        // ACT
        mockMvc.perform(get(path + "/Team1")
                        .header("Authorization", "Bearer " + getToken(invitee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Invitation accepted"));

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
    void acceptInvitation_invitationNotFound_shouldDoNothing() throws Exception {
        // ARRANGE
        User owner = createUser("John", "john@test.it");
        User invitee = createUser("Jane", "jane@test.it");
        User anotherUser = createUser("James", "james@test.it");
        Team team1 = createTeam("Team1", owner);
        Team team2 = createTeam("Team2", owner);
        Invitation invitation = createInvitation(team1, invitee);

        // ACT
        mockMvc.perform(get(path + "/Team2")
                        .header("Authorization", "Bearer " + getToken(invitee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Invitation not found"));

        // ASSERT
        // Invito originale ancora presente
        assertThat(invitationRepository.findById(invitation.getId())).isPresent();
        // Utente non aggiunto al team
        assertThat(team1.getMembers()).doesNotContain(invitee);
        assertThat(invitee.hasTeam()).isFalse();
    }

    @Test
    void acceptInvitation_teamAlreadyRegisteredInActiveHackathon_shouldThrowException() throws Exception {
        // ARRANGE
        // Crea hackathon con iscrizioni ancora aperte (sub deadline futura)
        LocalDate subscriptionDeadline = LocalDate.now().plusDays(5);
        Period period = new Period(LocalDate.now().plusDays(6), LocalDate.now().plusDays(10));
        Hackathon hackathon = createHackathon(subscriptionDeadline, period);

        User owner = createUser("John", "john@test.it");
        User invitee = createUser("Jane", "jane@test.it");
        Team team = createTeam("Team1", owner);
        hackathon.registerTeam(team);
        hackathonRepository.save(hackathon);

        Invitation invitation = createInvitation(team, invitee);

        // ACT & ASSERT
        mockMvc.perform(get(path + "/Team1")
                        .header("Authorization", "Bearer " + getToken(invitee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Can't accept, team in a active hackathon"));

        // Verifica che l'invito sia ancora presente e l'utente non sia stato aggiunto
        assertThat(invitationRepository.findById(invitation.getId())).isPresent();
        assertThat(team.getMembers()).doesNotContain(invitee);
        assertThat(invitee.hasTeam()).isFalse();
    }

    @Test
    void acceptInvitation_userAlreadyInAnotherTeam_shouldThrowException() throws Exception {
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
        mockMvc.perform(get(path + "/Team1")
                        .header("Authorization", "Bearer " + getToken(invitee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Can't accept, user already has team"));

        // Verifica che l'invito sia ancora presente e l'utente non sia stato spostato
        assertThat(invitationRepository.findById(invitation.getId())).isPresent();
        assertThat(team1.getMembers()).doesNotContain(invitee);
        assertThat(invitee.getTeam().getName()).isEqualTo("Team2");
    }

    private User createUser(String name, String email) {
        return userRepository.save(new User(name, email, "password"));
    }

    private Team createTeam(String teamName, User owner) {
        return teamRepository.save(new Team(teamName, owner));
    }

    private Invitation createInvitation(Team team, User invitee) {
        return invitationRepository.save(new Invitation(LocalDate.now(), team, invitee));
    }

    private Hackathon createHackathon(LocalDate subscriptionDeadline, Period period) {
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
                .maxTeamSize(5)
                .requirements("Regolamento...")
                .prize(1000.0)
                .organizer(organizer)
                .judge(judge)
                .mentors(mentors)
                .build();

        return hackathonRepository.save(hackathon);
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
