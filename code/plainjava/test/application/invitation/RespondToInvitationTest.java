package plainjava.test.application.invitation;

import plainjava.src.application.invitation.InvitationHandler;
import plainjava.src.application.invitation.InvitationHandlerImpl;
import plainjava.src.domain.invitation.domain.Invitation;
import plainjava.src.domain.invitation.repository.InvitationRepository;
import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.team.repository.TeamRepository;
import plainjava.src.domain.user.model.User;
import plainjava.src.domain.user.repository.UserRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaHackathonRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaInvitationRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaTeamRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaUserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RespondToInvitationTest {

    public static void test() {
        System.out.println("=====================================================");
        System.out.println("RespondToInvitationTest");
        successDeclineInvitation();
        invitationNotFoundDeclineInvitation();
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
            System.out.println(":( Failure Test: Invitation Declined");
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
            System.out.println(":( Failure Test: Invitation not found");
        }
    }
}
