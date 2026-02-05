package plainjava.test.application.hackathon;

import plainjava.src.application.hackathon.SubmissionHandler;
import plainjava.src.application.hackathon.SubmissionHandlerImpl;
import plainjava.src.domain.hackathon.model.Hackathon;
import plainjava.src.domain.hackathon.model.Submission;
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

public class SubmissionTest {

    public static void submissionTest() {
        successAddSubmissionTest();
        successUpdateSubmissionTest();
        teamOrHackathonNotFoundAddSubmissionTest();
        teamOrHackathonNotFoundUpdateSubmissionTest();
        alreadyAddedSubmissionTest();
        submissionNotFoundTest();
    }

    private static void successAddSubmissionTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        TeamRepository teamRepository = new JavaTeamRepository();
        SubmissionHandler handler = new SubmissionHandlerImpl(hackathonRepository, teamRepository);
        Team team = teamTestEntity("test");
        teamRepository.save(team);
        Hackathon hackathon = hackathonTestEntity(
                LocalDate.now().minusDays(5),
                new Period(LocalDate.now(), LocalDate.now().plusDays(2)),
                4);
        hackathon.changeState(new ProgressState(hackathon));
        hackathonRepository.save(hackathon);

        // ACT
        try {
            handler.addSubmission("test", 1L, submissionTestEntity());
        } catch (Exception e) {
        }

        // ASSERT
        System.out.println(
                hackathon.getSubmission(team) != null ?
                ":) Success Test: Add Submission" : ":( Failed Test: Add Submission");
    }

    private static void successUpdateSubmissionTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        TeamRepository teamRepository = new JavaTeamRepository();
        SubmissionHandler handler = new SubmissionHandlerImpl(hackathonRepository, teamRepository);
        Team team = teamTestEntity("test");
        teamRepository.save(team);
        Hackathon hackathon = hackathonTestEntity(
                LocalDate.now().minusDays(5),
                new Period(LocalDate.now(), LocalDate.now().plusDays(2)),
                4);
        hackathon.changeState(new ProgressState(hackathon));
        hackathonRepository.save(hackathon);

        // ACT
        try {
            handler.addSubmission("test", 1L, submissionTestEntity());
            handler.updateSubmission("test", 1L, submissionTestEntity());
        } catch (Exception e) {
            System.out.println(":( Failed Test: Update Submission");
            return;
        }

        // ASSERT
        System.out.println(":) Success Test: Update Submission");
    }

    private static void teamOrHackathonNotFoundAddSubmissionTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        TeamRepository teamRepository = new JavaTeamRepository();
        SubmissionHandler handler = new SubmissionHandlerImpl(hackathonRepository, teamRepository);
        Team team = teamTestEntity("test");
        teamRepository.save(team);
        Hackathon hackathon = hackathonTestEntity(
                LocalDate.now().minusDays(5),
                new Period(LocalDate.now(), LocalDate.now().plusDays(2)),
                4);
        hackathon.changeState(new ProgressState(hackathon));
        hackathonRepository.save(hackathon);

        // ACT
        try {
            handler.addSubmission("test2", 3L, submissionTestEntity());
        } catch (Exception e) {
        }

        // ASSERT
        System.out.println(
                hackathon.getSubmission(team) == null ?
                        ":) Success Test: Team or Hackathon not found (Add)" : ":( Failed Test: Team or Hackathon not found (Add)");
    }

    private static void teamOrHackathonNotFoundUpdateSubmissionTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        TeamRepository teamRepository = new JavaTeamRepository();
        SubmissionHandler handler = new SubmissionHandlerImpl(hackathonRepository, teamRepository);
        Team team = teamTestEntity("test");
        teamRepository.save(team);
        Hackathon hackathon = hackathonTestEntity(
                LocalDate.now().minusDays(5),
                new Period(LocalDate.now(), LocalDate.now().plusDays(2)),
                4);
        hackathon.changeState(new ProgressState(hackathon));
        hackathonRepository.save(hackathon);

        // ACT
        try {
            handler.addSubmission("test", 1L, submissionTestEntity());
            handler.updateSubmission("test2", 3L, submissionTestEntity());
        } catch (Exception e) {
            System.out.println(":) Success Test: Team or Hackathon not found (Update)");
            return;
        }

        // ASSERT
        System.out.println(":( Failed Test: Team or Hackathon not found (Update)");
    }

    private static void alreadyAddedSubmissionTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        TeamRepository teamRepository = new JavaTeamRepository();
        SubmissionHandler handler = new SubmissionHandlerImpl(hackathonRepository, teamRepository);
        Team team = teamTestEntity("test");
        teamRepository.save(team);
        Hackathon hackathon = hackathonTestEntity(
                LocalDate.now().minusDays(5),
                new Period(LocalDate.now(), LocalDate.now().plusDays(2)),
                4);
        hackathon.changeState(new ProgressState(hackathon));
        hackathonRepository.save(hackathon);

        // ACT
        try {
            handler.addSubmission("test", 1L, submissionTestEntity());
            handler.addSubmission("test", 1L, submissionTestEntity());
        } catch (Exception e) {
            System.out.println(":) Success Test: Submission already added");
            return;
        }

        // ASSERT
        System.out.println(":( Failed Test: Submission already added");
    }

    private static void submissionNotFoundTest() {
        // ARRANGE
        HackathonRepository hackathonRepository = new JavaHackathonRepository();
        TeamRepository teamRepository = new JavaTeamRepository();
        SubmissionHandler handler = new SubmissionHandlerImpl(hackathonRepository, teamRepository);
        Team team = teamTestEntity("test");
        teamRepository.save(team);
        Hackathon hackathon = hackathonTestEntity(
                LocalDate.now().minusDays(5),
                new Period(LocalDate.now(), LocalDate.now().plusDays(2)),
                4);
        hackathon.changeState(new ProgressState(hackathon));
        hackathonRepository.save(hackathon);

        // ACT
        try {
            handler.updateSubmission("test", 1L, submissionTestEntity());
        } catch (Exception e) {
            System.out.println(":) Success Test: Submission not added");
            return;
        }

        // ASSERT
        System.out.println(":( Failed Test: Submission not added");
    }

    private static Submission submissionTestEntity() {
        return new Submission(new File("test.txt"));
    }

    private static Team teamTestEntity(String teamName) {
        User user = new User("test", "test@test.test");
        User user2 = new User("test2", "test2@test.test");
        Team team = new Team(teamName, user);
        team.addMember(user2);
        return team;
    }

    private static Hackathon hackathonTestEntity(LocalDate subDeadline, Period period, int maxTeamSize) {
        Organizer organizer = new Organizer("organizer", "organizer@test.test");
        Judge judge = new Judge("judge", "judge@test.test");
        Mentor mentor1 = new Mentor("mentor1", "mentor1@test.test");
        Mentor mentor2 = new Mentor("mentor2", "mentor2@test.test");
        List<Mentor> mentors = new ArrayList<>();
        mentors.add(mentor1);
        mentors.add(mentor2);

        return new Hackathon("test", subDeadline, period, maxTeamSize, "", 10.0, organizer, judge, new HashSet<>(mentors));
    }
}
