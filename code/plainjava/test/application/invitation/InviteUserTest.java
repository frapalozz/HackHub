package plainjava.test.application.invitation;

import plainjava.src.application.invitation.InvitationHandler;
import plainjava.src.application.invitation.InvitationHandlerImpl;
import plainjava.src.domain.invitation.domain.InvitationId;
import plainjava.src.domain.invitation.repository.InvitationRepository;
import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.team.repository.TeamRepository;
import plainjava.src.domain.user.model.User;
import plainjava.src.domain.user.repository.UserRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaHackathonRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaInvitationRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaTeamRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaUserRepository;

import java.util.List;

public class InviteUserTest {

    public static void test() {
        System.out.println("=====================================================");
        System.out.println("InviteUserTest");
        successInviteTest();
        invalidEmailTest();
        userNotFoundTest();
        invitationAlreadyExistsTest();
    }

    private static void successInviteTest() {
        // ARRANGE
        UserRepository userRepo = new JavaUserRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        InvitationHandler handler = new InvitationHandlerImpl(userRepo, invitationRepo,teamRepo, new JavaHackathonRepository());
        User user1 = new User("John", "Doe@test.test");
        User user2 = new User("Jane", "Doi@test.test");
        Team team1 = new Team("Team 1", user1);
        userRepo.save(user1);
        userRepo.save(user2);
        teamRepo.save(team1);

        // ACT
        try {
            handler.inviteUser(user2.getEmail(), team1.getName());
        } catch (Exception e) {
        }

        // ASSERT
        if(invitationRepo.existsById(new InvitationId(team1, user2))) {
            System.out.println(":) Success Test: Invite User");
        } else {
            System.out.println(":( Failure Test: Invite User");
        }
    }

    private static void invalidEmailTest() {
        // ARRANGE
        UserRepository userRepo = new JavaUserRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        InvitationHandler handler = new InvitationHandlerImpl(userRepo, invitationRepo,teamRepo, new JavaHackathonRepository());
        User user1 = new User("John", "Doe@test.test");
        User user2 = new User("Jane", "Doi@test");
        Team team1 = new Team("Team 1", user1);
        userRepo.save(user1);
        userRepo.save(user2);
        teamRepo.save(team1);

        // ACT
        try {
            handler.inviteUser(user2.getEmail(), team1.getName());
        } catch (Exception e) {
        }

        // ASSERT
        if(!invitationRepo.existsById(new InvitationId(team1, user2))) {
            System.out.println(":) Success Test: Invalid Email");
        } else {
            System.out.println(":( Failure Test: Invalid Email");
        }
    }

    private static void userNotFoundTest() {
        // ARRANGE
        UserRepository userRepo = new JavaUserRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        InvitationHandler handler = new InvitationHandlerImpl(userRepo, invitationRepo,teamRepo, new JavaHackathonRepository());
        User user1 = new User("John", "Doe@test.test");
        User user2 = new User("Jane", "Doi@test.test");
        Team team1 = new Team("Team 1", user1);
        userRepo.save(user1);
        userRepo.save(user2);
        teamRepo.save(team1);

        // ACT
        try {
            handler.inviteUser(user2.getEmail()+"ss", team1.getName());
        } catch (Exception e) {
        }

        // ASSERT
        if(!invitationRepo.existsById(new InvitationId(team1, user2))) {
            System.out.println(":) Success Test: User not found");
        } else {
            System.out.println(":( Failure Test: User not found");
        }
    }

    private static void invitationAlreadyExistsTest() {
        // ARRANGE
        UserRepository userRepo = new JavaUserRepository();
        TeamRepository teamRepo = new JavaTeamRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        InvitationHandler handler = new InvitationHandlerImpl(userRepo, invitationRepo,teamRepo, new JavaHackathonRepository());
        User user1 = new User("John", "Doe@test.test");
        User user2 = new User("Jane", "Doi@test.test");
        Team team1 = new Team("Team 1", user1);
        userRepo.save(user1);
        userRepo.save(user2);
        teamRepo.save(team1);

        // ACT
        try {
            handler.inviteUser(user2.getEmail(), team1.getName());
            handler.inviteUser(user2.getEmail(), team1.getName());
        } catch (Exception e) {
        }

        // ASSERT
        if((invitationRepo.findAll(List.of(new InvitationId(team1, user2))).size() == 1)) {
            System.out.println(":) Success Test: Invitation already exist");
        } else {
            System.out.println(":( Failure Test: Invitation already exist");
        }
    }
}
