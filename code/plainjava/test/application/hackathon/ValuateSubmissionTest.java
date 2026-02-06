package plainjava.test.application.hackathon;

import plainjava.src.application.hackathon.SubmissionHandler;
import plainjava.src.application.hackathon.SubmissionHandlerImpl;
import plainjava.src.domain.hackathon.model.Hackathon;
import plainjava.src.domain.hackathon.model.Submission;
import plainjava.src.domain.hackathon.model.state.EvaluationState;
import plainjava.src.domain.hackathon.model.state.ProgressState;
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

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ValuateSubmissionTest {

    public static void test() {
        System.out.println("=====================================================");
        System.out.println("ValuateSubmissionTest");
        successValuateSubmissionTest();
        dataNotValidTest();
        teamNotFoundTest();
        hackathonNotFoundTest();
        hackathonNotInEvaluationTest();
    }

    private static void successValuateSubmissionTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        TeamRepository teamRepository = new JavaTeamRepository();
        SubmissionHandler handler = new SubmissionHandlerImpl(hackathonRepository, teamRepository);
        Hackathon hackathon = hackathonTestEntity();
        Team team = new Team("test", new User("test", "test@test.test"));
        teamRepository.save(team);
        hackathon.registerTeam(team);
        hackathon.changeState(new ProgressState(hackathon));
        hackathonRepository.save(hackathon);
        hackathon.addSubmission(team, new Submission(new File("test")));
        hackathon.changeState(new EvaluationState(hackathon));

        // ACT
        try {
            handler.valuateSubmission(hackathon.getId(), team.getName(), 5, "sdqdqw");
        } catch (Exception e) {
        }

        // ASSERT
        if(hackathon.getSubmission(team).getValuation() != null) {
            System.out.println(":) Success Test: Valuate Submission");
        } else {
            System.out.println("X Failure Test: Valuate Submission");
        }
    }

    private static void dataNotValidTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        TeamRepository teamRepository = new JavaTeamRepository();
        SubmissionHandler handler = new SubmissionHandlerImpl(hackathonRepository, teamRepository);
        Hackathon hackathon = hackathonTestEntity();
        Team team = new Team("test", new User("test", "test@test.test"));
        teamRepository.save(team);
        hackathon.registerTeam(team);
        hackathon.changeState(new ProgressState(hackathon));
        hackathonRepository.save(hackathon);
        hackathon.addSubmission(team, new Submission(new File("test")));
        hackathon.changeState(new EvaluationState(hackathon));

        // ACT
        try {
            handler.valuateSubmission(hackathon.getId(), team.getName(), -1, "");
        } catch (Exception e) {
        }

        // ASSERT
        if(hackathon.getSubmission(team).getValuation() == null) {
            System.out.println(":) Success Test: Data not valid");
        } else {
            System.out.println("X Failure Test: Data not valid");
        }
    }

    private static void teamNotFoundTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        TeamRepository teamRepository = new JavaTeamRepository();
        SubmissionHandler handler = new SubmissionHandlerImpl(hackathonRepository, teamRepository);
        Hackathon hackathon = hackathonTestEntity();
        Team team = new Team("test", new User("test", "test@test.test"));
        teamRepository.save(team);
        hackathon.registerTeam(team);
        hackathon.changeState(new ProgressState(hackathon));
        hackathonRepository.save(hackathon);
        hackathon.addSubmission(team, new Submission(new File("test")));
        hackathon.changeState(new EvaluationState(hackathon));

        // ACT
        try {
            handler.valuateSubmission(hackathon.getId(), team.getName()+"ss", 5, "sdqdqw");
        } catch (Exception e) {
            System.out.println(":) Success Test: team not found");
            return;
        }

        // ASSERT
        System.out.println("X Failure Test: team not found");
    }

    private static void hackathonNotFoundTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        TeamRepository teamRepository = new JavaTeamRepository();
        SubmissionHandler handler = new SubmissionHandlerImpl(hackathonRepository, teamRepository);
        Hackathon hackathon = hackathonTestEntity();
        Team team = new Team("test", new User("test", "test@test.test"));
        teamRepository.save(team);
        hackathon.registerTeam(team);
        hackathon.changeState(new ProgressState(hackathon));
        hackathonRepository.save(hackathon);
        hackathon.addSubmission(team, new Submission(new File("test")));
        hackathon.changeState(new EvaluationState(hackathon));

        // ACT
        try {
            handler.valuateSubmission(hackathon.getId()+5, team.getName(), 5, "sdqdqw");
        } catch (Exception e) {
            System.out.println(":) Success Test: Hackathon not found");
            return;
        }

        // ASSERT
        System.out.println("X Failure Test: Hackathon not found");
    }

    private static void hackathonNotInEvaluationTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        TeamRepository teamRepository = new JavaTeamRepository();
        SubmissionHandler handler = new SubmissionHandlerImpl(hackathonRepository, teamRepository);
        Hackathon hackathon = hackathonTestEntity();
        Team team = new Team("test", new User("test", "test@test.test"));
        teamRepository.save(team);
        hackathon.registerTeam(team);
        hackathon.changeState(new ProgressState(hackathon));
        hackathonRepository.save(hackathon);
        hackathon.addSubmission(team, new Submission(new File("test")));

        // ACT
        try {
            handler.valuateSubmission(hackathon.getId(), team.getName(), 5, "sdqdqw");
        } catch (Exception e) {
        }

        // ASSERT
        if(hackathon.getSubmission(team).getValuation() == null) {
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
