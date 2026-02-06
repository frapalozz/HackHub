package plainjava.test.application.team;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import plainjava.src.application.team.RegisterTeamHandler;
import plainjava.src.application.team.RegisterTeamHandlerImpl;
import plainjava.src.domain.hackathon.model.Hackathon;
import plainjava.src.domain.hackathon.repository.HackathonRepository;
import plainjava.src.domain.staffMember.model.Judge;
import plainjava.src.domain.staffMember.model.Mentor;
import plainjava.src.domain.staffMember.model.Organizer;
import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.team.repository.TeamRepository;
import plainjava.src.domain.user.model.User;
import plainjava.src.domain.utils.Period;
import plainjava.src.infrastructure.persistence.plainJava.JavaHackathonRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaTeamRepository;

public class RegisterTeamTest {
    
    public static void test() {
        System.out.println("=====================================================");
        System.out.println("RegisterTeamTest");
        successRegisterTeamTest();
        teamOrHackathonNotFoundTest();
        hackathonSubscriptionClosedTest();
        hackathonRequirementsNotMetTest();
        teamAlreadyRegisteredTest();
    }

    private static void successRegisterTeamTest() {
        // ARRANGE
        HackathonRepository hackathonRepo = new JavaHackathonRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        RegisterTeamHandler handler = new RegisterTeamHandlerImpl(hackathonRepo, teamRepo);
        Hackathon hackathon = createTestHackathon(
                LocalDate.parse("2026-02-15"),
                new Period(LocalDate.parse("2026-02-16"), LocalDate.parse("2026-02-28")),
                4);
        hackathonRepo.save(hackathon);
        Team team = createTestTeam("testTeam");
        teamRepo.save(team);

        // ACT
        try {
            handler.registerTeam("testTeam", hackathon.getId());
        } catch (Exception e) {
        }

        // ASSERT
        System.out.println(
            hackathon.getTeams().contains(team) ?
                ":) Success Test: Team registered to hackathon" : ":( Failed Test: Team registered to hackathon"
        );
    }

    private static void teamOrHackathonNotFoundTest() {
        // ARRANGE
        HackathonRepository hackathonRepo = new JavaHackathonRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        RegisterTeamHandler handler = new RegisterTeamHandlerImpl(hackathonRepo, teamRepo);
        Hackathon hackathon = createTestHackathon(
                LocalDate.parse("2026-02-15"),
                new Period(LocalDate.parse("2026-02-16"), LocalDate.parse("2026-02-28")),
                4);
        hackathonRepo.save(hackathon);
        Team team = createTestTeam("testTeam");
        teamRepo.save(team);

        // ACT
        try {
            handler.registerTeam("testTeam2", hackathon.getId()+1);
        } catch (Exception e) {
        }

        // ASSERT
        System.out.println(
                !hackathon.getTeams().contains(team) ?
                        ":) Success Test: Team or Hackathon not found" : ":( Failed Test: Team or Hackathon not found"
        );
    }

    private static void hackathonSubscriptionClosedTest() {
        // ARRANGE
        HackathonRepository hackathonRepo = new JavaHackathonRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        RegisterTeamHandler handler = new RegisterTeamHandlerImpl(hackathonRepo, teamRepo);
        Hackathon hackathon = createTestHackathon(
                LocalDate.parse("2026-02-04"),
                new Period(LocalDate.parse("2026-02-16"), LocalDate.parse("2026-02-28")),
                4);
        hackathonRepo.save(hackathon);
        Team team = createTestTeam("testTeam");
        teamRepo.save(team);

        // ACT
        try {
            handler.registerTeam("testTeam", hackathon.getId());
        } catch (Exception e) {
        }

        // ASSERT
        System.out.println(
                !hackathon.getTeams().contains(team) ?
                        ":) Success Test: subscription closed" : ":( Failed Test: subscription closed"
        );
    }

    private static void hackathonRequirementsNotMetTest() {
        // ARRANGE
        HackathonRepository hackathonRepo = new JavaHackathonRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        RegisterTeamHandler handler = new RegisterTeamHandlerImpl(hackathonRepo, teamRepo);
        Hackathon hackathon = createTestHackathon(
                LocalDate.parse("2026-02-15"),
                new Period(LocalDate.parse("2026-02-16"), LocalDate.parse("2026-02-28")),
                1);
        hackathonRepo.save(hackathon);
        Team team = createTestTeam("testTeam");
        teamRepo.save(team);

        // ACT
        try {
            handler.registerTeam("testTeam", hackathon.getId());
        } catch (Exception e) {
        }

        // ASSERT
        System.out.println(
                !hackathon.getTeams().contains(team) ?
                        ":) Success Test: requirements not met" : ":( Failed Test: requirements not met"
        );
    }

    private static void teamAlreadyRegisteredTest() {
        // ARRANGE
        HackathonRepository hackathonRepo = new JavaHackathonRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        RegisterTeamHandler handler = new RegisterTeamHandlerImpl(hackathonRepo, teamRepo);
        Hackathon hackathon = createTestHackathon(
                LocalDate.parse("2026-02-15"),
                new Period(LocalDate.parse("2026-02-16"), LocalDate.parse("2026-02-28")),
                4);
        hackathonRepo.save(hackathon);
        Team team = createTestTeam("testTeam");
        teamRepo.save(team);
        hackathon.registerTeam(team);

        // ACT
        try {
            handler.registerTeam("testTeam", hackathon.getId());
        } catch (Exception e) {
            System.out.println(":) Success Test: team already registered");
            return;
        }

        // ASSERT
        System.out.println(":( Failed Test: team already registered");
    }

    private static Hackathon createTestHackathon(LocalDate subDeadline, Period period, int maxTeamSize) {
        Organizer organizer = new Organizer("organizer", "organizer@test.test");
        Judge judge = new Judge("judge", "judge@test.test");
        Mentor mentor1 = new Mentor("mentor1", "mentor1@test.test");
        Mentor mentor2 = new Mentor("mentor2", "mentor2@test.test");
        List<Mentor> mentors = new ArrayList<>();
        mentors.add(mentor1);
        mentors.add(mentor2);

        return new Hackathon("test", subDeadline, period, maxTeamSize, "", 10.0, organizer, judge, new HashSet<>(mentors));
    }

    private static Team createTestTeam(String teamName) {
        User user = new User("test", "test@test.test");
        User user2 = new User("test2", "test2@test.test");
        Team team = new Team(teamName, user);
        team.addMember(user2);
        return team;
    }
}
