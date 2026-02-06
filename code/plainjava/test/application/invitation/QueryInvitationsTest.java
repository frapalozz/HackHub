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

public class QueryInvitationsTest {

    public static void test() {
        System.out.println("=====================================================");
        System.out.println("QueryInvitationsTest");
        getZeroInvitation();
        getOneInvitation();
        getTwoInvitations();
    }

    private static void getZeroInvitation() {

        // ARRANGE
        UserRepository userRepo = new JavaUserRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        InvitationHandler handler = new InvitationHandlerImpl(userRepo, invitationRepo, teamRepo, new JavaHackathonRepository());
        User user1 = new User("John", "Doe@test.test");
        userRepo.save(user1);
        List<Invitation> invitationsFound = new ArrayList<>();

        // ACT
        try {
            invitationsFound = handler.getInvitations(user1.getEmail());
        } catch (Exception e) {
        }

        // ASSERT
        if(invitationsFound.isEmpty()) {
            System.out.println(":) Success Test: Zero Invitations");
        } else {
            System.out.println("X Failure Test: Zero Invitations");
        }
    }

    private static void getOneInvitation() {
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
            invitationsFound = handler.getInvitations(user2.getEmail());
        } catch (Exception e) {
        }

        // ASSERT
        if(invitationsFound.size() == 1) {
            System.out.println(":) Success Test: One Invitations");
        } else {
            System.out.println("X Failure Test: One Invitations");
        }
    }

    private static void getTwoInvitations() {
        // ARRANGE
        UserRepository userRepo = new JavaUserRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        InvitationHandler handler = new InvitationHandlerImpl(userRepo, invitationRepo, teamRepo, new JavaHackathonRepository());
        User user1 = new User("John", "Doe@test.test");
        User user2 = new User("Jane", "Doi@test.test");
        User user3 = new User("Jane", "Dou@test.test");
        Team team1 = new Team("Team 1", user1);
        Team team2 = new Team("Team 2", user2);
        userRepo.save(user1);
        userRepo.save(user2);
        userRepo.save(user3);
        teamRepo.save(team1);
        teamRepo.save(team2);
        Invitation invitation1 = new Invitation(LocalDate.now(), team1, user3);
        Invitation invitation2 = new Invitation(LocalDate.now(), team2, user3);
        invitationRepo.save(invitation1);
        invitationRepo.save(invitation2);
        List<Invitation> invitationsFound = new ArrayList<>();

        // ACT
        try {
            invitationsFound = handler.getInvitations(user3.getEmail());
        } catch (Exception e) {
        }

        // ASSERT
        if(invitationsFound.size() == 2) {
            System.out.println(":) Success Test: Two Invitations");
        } else {
            System.out.println("X Failure Test: Two Invitations");
        }
    }
}
