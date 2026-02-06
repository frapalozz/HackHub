package plainjava.test.application.hackathon;

import plainjava.src.application.hackathon.HackathonHandler;
import plainjava.src.application.hackathon.HackathonHandlerImpl;
import plainjava.src.domain.hackathon.model.Hackathon;
import plainjava.src.domain.hackathon.model.state.EvaluationState;
import plainjava.src.domain.hackathon.repository.HackathonRepository;
import plainjava.src.domain.staffMember.model.Judge;
import plainjava.src.domain.staffMember.model.Mentor;
import plainjava.src.domain.staffMember.model.Organizer;
import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.user.model.User;
import plainjava.src.domain.utils.Period;
import plainjava.src.infrastructure.persistence.plainJava.JavaHackathonRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DeclareWinnerTest {

    public static void test() {
        System.out.println("=====================================================");
        System.out.println("DeclareWinnerTest");
        successDeclareWinnerTest();
        teamNotFoundTest();
        hackathonNotFoundTest();
        hackathonNotInValuationTest();
    }

    private static void successDeclareWinnerTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        HackathonHandler handler = new HackathonHandlerImpl(hackathonRepository);
        Hackathon hackathon = hackathonTestEntity();
        Team team = new Team("team", new User("test", "test@test.test"));
        hackathon.registerTeam(team);
        hackathon.changeState(new EvaluationState(hackathon));
        hackathonRepository.save(hackathon);

        // ACT
        try {
            handler.declareWinner(hackathon.getId(), team.getName());
        } catch (Exception e) {

        }

        // ASSERT
        if(hackathon.getWinner() == team) {
            System.out.println(":) Success Test: Winner Declare");
        } else {
            System.out.println("X Failure Test: Winner Declare");
        }
    }

    private static void teamNotFoundTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        HackathonHandler handler = new HackathonHandlerImpl(hackathonRepository);
        Hackathon hackathon = hackathonTestEntity();
        Team team = new Team("team", new User("test", "test@test.test"));
        hackathon.registerTeam(team);
        hackathon.changeState(new EvaluationState(hackathon));
        hackathonRepository.save(hackathon);

        // ACT
        try {
            handler.declareWinner(hackathon.getId(), team.getName()+"w");
        } catch (Exception e) {

        }

        // ASSERT
        if(hackathon.getWinner() == null) {
            System.out.println(":) Success Test: Team not found");
        } else {
            System.out.println("X Failure Test: Team not found");
        }
    }

    private static void hackathonNotFoundTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        HackathonHandler handler = new HackathonHandlerImpl(hackathonRepository);
        Hackathon hackathon = hackathonTestEntity();
        Team team = new Team("team", new User("test", "test@test.test"));
        hackathon.registerTeam(team);
        hackathon.changeState(new EvaluationState(hackathon));
        hackathonRepository.save(hackathon);

        // ACT
        try {
            handler.declareWinner(hackathon.getId()+999, team.getName());
        } catch (Exception e) {

        }

        // ASSERT
        if(hackathon.getWinner() == null) {
            System.out.println(":) Success Test: Hackathon not found");
        } else {
            System.out.println("X Failure Test: Hackathon not found");
        }
    }

    private static void hackathonNotInValuationTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        HackathonHandler handler = new HackathonHandlerImpl(hackathonRepository);
        Hackathon hackathon = hackathonTestEntity();
        Team team = new Team("team", new User("test", "test@test.test"));
        hackathon.registerTeam(team);
        hackathonRepository.save(hackathon);

        // ACT
        try {
            handler.declareWinner(hackathon.getId(), team.getName());
        } catch (Exception e) {

        }

        // ASSERT
        if(hackathon.getWinner() == null) {
            System.out.println(":) Success Test: Hackathon not in evaluation");
        } else {
            System.out.println("X Failure Test: Hackathon not in evaluation");
        }
    }

    private static Hackathon hackathonTestEntity() {
        Organizer organizer = new Organizer("organizer", "organizer@test.test");
        Judge judge = new Judge("judge", "judge@test.test");
        Mentor mentor1 = new Mentor("mentor1", "mentor1@test.test");
        Mentor mentor2 = new Mentor("mentor2", "mentor2@test.test");
        List<Mentor> mentors = new ArrayList<>();
        mentors.add(mentor1);
        mentors.add(mentor2);

        return new Hackathon(
                "test",
                LocalDate.now(),
                new Period(LocalDate.now(), LocalDate.now().plusDays(5)),
                4,
                "",
                10.0,
                organizer,
                judge,
                new HashSet<>(mentors));
    }
}
