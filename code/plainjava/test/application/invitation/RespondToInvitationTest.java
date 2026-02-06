package plainjava.test.application.invitation;

import plainjava.src.application.invitation.InvitationHandler;
import plainjava.src.application.invitation.InvitationHandlerImpl;
import plainjava.src.domain.hackathon.model.Hackathon;
import plainjava.src.domain.hackathon.repository.HackathonRepository;
import plainjava.src.domain.invitation.domain.Invitation;
import plainjava.src.domain.invitation.repository.InvitationRepository;
import plainjava.src.domain.staffMember.model.Judge;
import plainjava.src.domain.staffMember.model.Mentor;
import plainjava.src.domain.staffMember.model.Organizer;
import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.team.repository.TeamRepository;
import plainjava.src.domain.user.model.User;
import plainjava.src.domain.user.repository.UserRepository;
import plainjava.src.domain.utils.Period;
import plainjava.src.infrastructure.persistence.plainJava.JavaHackathonRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaInvitationRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaTeamRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaUserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RespondToInvitationTest {

    public static void test() {
        System.out.println("=====================================================");
        System.out.println("RespondToInvitationTest");
        successDeclineInvitation();
        invitationNotFoundDeclineInvitation();
        successAcceptInvitation();
        invitationNotFoundAcceptInvitation();
        cantAcceptInvitationTeamInActiveHackathon();
        userAlreadyInATeam();
    }

    private static void successDeclineInvitation() {
        // ARRANGE
        UserRepository userRepo = new JavaUserRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        InvitationHandler handler = new InvitationHandlerImpl(userRepo, invitationRepo, teamRepo, new JavaHackathonRepository());
        User user1 = new User("John", "Doe@test.test");
        User user2 = new User("Jane", "Doi@test.test");
        Team team1 = new Team("Team 1", user1);
        userRepo.save(user1);
        userRepo.save(user2);
        teamRepo.save(team1);
        Invitation invitation = new Invitation(LocalDate.now(), team1, user2);
        invitationRepo.save(invitation);
        List<Invitation> invitationsFound = new ArrayList<>();

        // ACT
        try {
            handler.declineInvitation(invitation.getId());
            invitationsFound = handler.getInvitations(user2.getEmail());
        } catch (Exception e) {
        }

        // ASSERT
        if(invitationsFound.isEmpty()) {
            System.out.println(":) Success Test: Invitation Declined");
        } else {
            System.out.println("X Failure Test: Invitation Declined");
        }
    }

    private static void invitationNotFoundDeclineInvitation() {
        // ARRANGE
        UserRepository userRepo = new JavaUserRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        InvitationHandler handler = new InvitationHandlerImpl(userRepo, invitationRepo, teamRepo, new JavaHackathonRepository());
        User user1 = new User("John", "Doe@test.test");
        User user2 = new User("Jane", "Doi@test.test");
        User user3 = new User("Jane", "Dou@test.test");
        Team team1 = new Team("Team 1", user1);
        userRepo.save(user1);
        userRepo.save(user2);
        userRepo.save(user3);
        teamRepo.save(team1);
        Invitation invitation = new Invitation(LocalDate.now(), team1, user2);
        Invitation invitation2 = new Invitation(LocalDate.now(), new Team("test", user3), user2);
        invitationRepo.save(invitation);
        List<Invitation> invitationsFound;

        // ACT
        try {
            handler.declineInvitation(invitation2.getId());
        } catch (Exception e) {
        } finally {
            invitationsFound = handler.getInvitations(user2.getEmail());
        }

        // ASSERT
        if(invitationsFound.size() == 1) {
            System.out.println(":) Success Test: Invitation not found");
        } else {
            System.out.println("X Failure Test: Invitation not found");
        }
    }

    private static void successAcceptInvitation() {
        // ARRANGE
        UserRepository userRepo = new JavaUserRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        InvitationHandler handler = new InvitationHandlerImpl(userRepo, invitationRepo, teamRepo, new JavaHackathonRepository());
        User user1 = new User("John", "Doe@test.test");
        User user2 = new User("Jane", "Doi@test.test");
        Team team1 = new Team("Team 1", user1);
        userRepo.save(user1);
        userRepo.save(user2);
        teamRepo.save(team1);
        Invitation invitation = new Invitation(LocalDate.now(), team1, user2);
        invitationRepo.save(invitation);
        List<Invitation> invitationsFound = new ArrayList<>();

        // ACT
        try {
            handler.acceptInvitation(invitation.getId());
            invitationsFound = handler.getInvitations(user2.getEmail());
        } catch (Exception e) {
        }

        // ASSERT
        if(invitationsFound.isEmpty() && user2.hasTeam()) {
            System.out.println(":) Success Test: Invitation Accepted");
        } else {
            System.out.println("X Failure Test: Invitation Accepted");
        }
    }

    private static void invitationNotFoundAcceptInvitation() {
        // ARRANGE
        UserRepository userRepo = new JavaUserRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        InvitationHandler handler = new InvitationHandlerImpl(userRepo, invitationRepo, teamRepo, new JavaHackathonRepository());
        User user1 = new User("John", "Doe@test.test");
        User user2 = new User("Jane", "Doi@test.test");
        User user3 = new User("Jane", "Dou@test.test");
        Team team1 = new Team("Team 1", user1);
        userRepo.save(user1);
        userRepo.save(user2);
        userRepo.save(user3);
        teamRepo.save(team1);
        Invitation invitation = new Invitation(LocalDate.now(), team1, user2);
        Invitation invitation2 = new Invitation(LocalDate.now(), new Team("test", user3), user2);
        invitationRepo.save(invitation);
        List<Invitation> invitationsFound;

        // ACT
        try {
            handler.acceptInvitation(invitation2.getId());
        } catch (Exception e) {
        } finally {
            invitationsFound = handler.getInvitations(user2.getEmail());
        }

        // ASSERT
        if(invitationsFound.size() == 1 && !user2.hasTeam()) {
            System.out.println(":) Success Test: Invitation not found2");
        } else {
            System.out.println("X Failure Test: Invitation not found2");
        }
    }

    private static void cantAcceptInvitationTeamInActiveHackathon() {
        // ARRANGE
        UserRepository userRepo = new JavaUserRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        HackathonRepository hackathonRepo = new JavaHackathonRepository();
        InvitationHandler handler = new InvitationHandlerImpl(userRepo, invitationRepo, teamRepo, hackathonRepo);
        User user1 = new User("John", "Doe@test.test");
        User user2 = new User("Jane", "Doi@test.test");
        Team team1 = new Team("Team 1", user1);
        userRepo.save(user1);
        userRepo.save(user2);
        teamRepo.save(team1);
        Hackathon hackathon = hackathonTestEntity();
        hackathon.registerTeam(team1);
        hackathonRepo.save(hackathon);
        Invitation invitation = new Invitation(LocalDate.now(), team1, user2);
        invitationRepo.save(invitation);
        List<Invitation> invitationsFound;

        // ACT
        try {
            handler.acceptInvitation(invitation.getId());

        } catch (Exception e) {
        } finally {
            invitationsFound = handler.getInvitations(user2.getEmail());
        }

        // ASSERT
        if(invitationsFound.size() == 1 && !user2.hasTeam()) {
            System.out.println(":) Success Test: Can't accept invitation, team in active hackathon");
        } else {
            System.out.println("X Failure Test: Can't accept invitation, team in active hackathon");
        }
    }

    private static void userAlreadyInATeam() {
        // ARRANGE
        UserRepository userRepo = new JavaUserRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        InvitationHandler handler = new InvitationHandlerImpl(userRepo, invitationRepo, teamRepo, new JavaHackathonRepository());
        User user1 = new User("John", "Doe@test.test");
        User user2 = new User("Jane", "Doi@test.test");
        Team team1 = new Team("Team 1", user1);
        Team team2 = new Team("Team 2", user2);
        user1.setTeam(team1);
        user2.setTeam(team2);
        userRepo.save(user1);
        userRepo.save(user2);
        teamRepo.save(team1);
        teamRepo.save(team2);
        Invitation invitation = new Invitation(LocalDate.now(), team1, user2);
        invitationRepo.save(invitation);
        List<Invitation> invitationsFound;

        // ACT
        try {
            handler.acceptInvitation(invitation.getId());
        } catch (Exception e) {
        } finally {
            invitationsFound = handler.getInvitations(user2.getEmail());
        }

        // ASSERT
        if(invitationsFound.size() == 1 && user2.getTeam().equals(team2)) {
            System.out.println(":) Success Test: Already in a Team");
        } else {
            System.out.println("X Failure Test: Already in a Team");
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
                new Period(LocalDate.now().plusDays(1), LocalDate.now().plusDays(5)),
                4,
                "",
                10.0,
                organizer,
                judge,
                new HashSet<>(mentors));
    }
}
