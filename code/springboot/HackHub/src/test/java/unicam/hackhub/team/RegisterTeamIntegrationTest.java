package unicam.hackhub.team;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import unicam.hackhub.application.team.RegisterTeamHandler;
import unicam.hackhub.config.DataInitializer;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.utils.Period;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class RegisterTeamIntegrationTest {

    @Autowired
    private RegisterTeamHandler registerTeamHandler;

    @Autowired
    private HackathonRepository hackathonRepository;

    @Autowired
    private TeamRepository teamRepository;

    @MockitoBean
    private DataInitializer dataInitializer;

    @Test
    void registerTeam_success_shouldAddTeamToHackathon() {
        // ARRANGE
        Hackathon hackathon = createTestHackathon(
                LocalDate.parse("2026-02-15"),           // iscrizioni aperte
                new Period(LocalDate.parse("2026-02-16"), LocalDate.parse("2026-02-28")),
                4                                        // max team size
        );
        hackathonRepository.save(hackathon);

        Team team = createTestTeam("TeamVincente");
        teamRepository.save(team);

        // ACT
        registerTeamHandler.registerTeam(team.getName(), hackathon.getId());

        // ASSERT
        Hackathon updatedHackathon = hackathonRepository.findById(hackathon.getId()).orElseThrow();
        assertThat(updatedHackathon.getTeams()).contains(team);
    }

    @Test
    void registerTeam_teamOrHackathonNotFound_shouldThrowException() {
        // ARRANGE
        Hackathon hackathon = createTestHackathon(
                LocalDate.parse("2026-02-15"),
                new Period(LocalDate.parse("2026-02-16"), LocalDate.parse("2026-02-28")),
                4
        );
        hackathonRepository.save(hackathon);
        Team team = createTestTeam("TeamEsistente");
        teamRepository.save(team);

        // ACT & ASSERT
        // team inesistente
        assertThrows(IllegalArgumentException.class,
                () -> registerTeamHandler.registerTeam("TeamInesistente", hackathon.getId()));

        // hackathon inesistente
        assertThrows(IllegalArgumentException.class,
                () -> registerTeamHandler.registerTeam(team.getName(), 9999L));
    }

    @Test
    void registerTeam_subscriptionClosed_shouldThrowException() {
        // ARRANGE
        Hackathon hackathon = createTestHackathon(
                LocalDate.parse("2026-02-04"),           // scaduta (oggi è 2026-02-12)
                new Period(LocalDate.parse("2026-02-16"), LocalDate.parse("2026-02-28")),
                4
        );
        hackathonRepository.save(hackathon);
        Team team = createTestTeam("TeamInRitardo");
        teamRepository.save(team);

        // ACT & ASSERT
        assertThrows(IllegalStateException.class,
                () -> registerTeamHandler.registerTeam(team.getName(), hackathon.getId()));

    }

    @Test
    void registerTeam_teamTooLarge_shouldThrowException() {
        // ARRANGE
        Hackathon hackathon = createTestHackathon(
                LocalDate.parse("2026-02-15"),
                new Period(LocalDate.parse("2026-02-16"), LocalDate.parse("2026-02-28")),
                1   // dimensione massima = 1
        );
        hackathonRepository.save(hackathon);

        Team team = createTestTeam("TeamTroppoGrande");  // ha 2 membri
        teamRepository.save(team);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> registerTeamHandler.registerTeam(team.getName(), hackathon.getId()));
        assertThat(hackathon.getTeams()).doesNotContain(team);
    }

    @Test
    void registerTeam_teamAlreadyRegistered_shouldThrowException() {
        // ARRANGE
        Hackathon hackathon = createTestHackathon(
                LocalDate.parse("2026-02-15"),
                new Period(LocalDate.parse("2026-02-16"), LocalDate.parse("2026-02-28")),
                4
        );
        hackathonRepository.save(hackathon);

        Team team = createTestTeam("TeamRegistrato");
        teamRepository.save(team);

        // Prima registrazione → successo
        registerTeamHandler.registerTeam(team.getName(), hackathon.getId());

        // ACT & ASSERT
        assertThrows(IllegalStateException.class,
                () -> registerTeamHandler.registerTeam(team.getName(), hackathon.getId()));
    }

    private Hackathon createTestHackathon(LocalDate subscriptionDeadline, Period period, int maxTeamSize) {
        Staff organizer = new Staff("organizer", "organizer@test.it");
        Staff judge = new Staff("judge", "judge@test.it");
        Staff mentor1 = new Staff("mentor1", "mentor1@test.it");
        Staff mentor2 = new Staff("mentor2", "mentor2@test.it");
        List<Staff> mentors = List.of(mentor1, mentor2);
        return new Hackathon(
                "Hackathon Test",
                subscriptionDeadline,
                period,
                maxTeamSize,
                "Regolamento...",
                1000.0,
                organizer,
                judge,
                new HashSet<>(mentors)
        );
    }

    private Team createTestTeam(String teamName) {
        User user1 = new User("user1", "user1@test.it");
        User user2 = new User("user2", "user2@test.it");
        Team team = new Team(teamName, user1);
        team.addMember(user2);
        return team;
    }
}
