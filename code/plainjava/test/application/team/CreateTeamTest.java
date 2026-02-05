package plainjava.test.application.team;

import plainjava.src.application.invitation.InvitationService;
import plainjava.src.application.team.CreateTeamHandler;
import plainjava.src.application.team.CreateTeamHandlerImpl;
import plainjava.src.domain.invitation.repository.InvitationRepository;
import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.team.repository.TeamRepository;
import plainjava.src.domain.user.model.User;
import plainjava.src.domain.user.repository.UserRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaInvitationRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaTeamRepository;
import plainjava.src.infrastructure.persistence.plainJava.JavaUserRepository;

public class CreateTeamTest {
    
    public static void createTeamTest() {
        userAlreadyInTeamTest();
        userNotFoundTest();
        teamNameAlreadyUsedTest();
        succesTeamCreationTest();
    }

    private static void userAlreadyInTeamTest() {
        // ARRANGE
        TeamRepository teamRepo = new JavaTeamRepository();
        UserRepository userRepo = new JavaUserRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        CreateTeamHandler handler = new CreateTeamHandlerImpl(
            userRepo, 
            teamRepo, 
            new InvitationService(userRepo, invitationRepo)
        );
        User user = new User("userTest", "userTest@test.test");
        Team team = new Team("test", user);
        user.setTeam(team);
        teamRepo.save(team);
        userRepo.save(user);

        // ACT
        try {
            handler.createTeam(user.getEmail(), "test2", null);
        } catch (Exception e) {

        }
        

        // ASSERT
        System.out.println(
            teamRepo.findById("test2") == null ?
                ":) Success Test: User already in Team" : ":( Failed Test: User already in Team"
        );
    }

    private static void userNotFoundTest() {
        // ARRANGE
        TeamRepository teamRepo = new JavaTeamRepository();
        UserRepository userRepo = new JavaUserRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        CreateTeamHandler handler = new CreateTeamHandlerImpl(
            userRepo, 
            teamRepo, 
            new InvitationService(userRepo, invitationRepo)
        );
        User user = new User("userTest", "userTest@test.test");
        Team team = new Team("test", user);
        user.setTeam(team);
        teamRepo.save(team);
        userRepo.save(user);

        // ACT
        try {
            handler.createTeam("notPresentId", "test2", null);
        } catch (Exception e) {

        }

        // ASSERT
        System.out.println(
            teamRepo.findById("test2") == null ?
                ":) Success Test: User not Found" : ":( Failed Test: User not Found"
        );
    }

    private static void teamNameAlreadyUsedTest() {
        // ARRANGE
        TeamRepository teamRepo = new JavaTeamRepository();
        UserRepository userRepo = new JavaUserRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        CreateTeamHandler handler = new CreateTeamHandlerImpl(
            userRepo, 
            teamRepo, 
            new InvitationService(userRepo, invitationRepo)
        );
        User user = new User("userTest", "userTest@test.test");
        Team team = new Team("test", user);
        user.setTeam(team);
        User user2 = new User("userTest2", "userTest2@test.test");
        teamRepo.save(team);
        userRepo.save(user);
        userRepo.save(user2);

        // ACT
        try {
            handler.createTeam(user2.getEmail(), "test", null);
        } catch (Exception e) {

        }

        // ASSERT
        System.out.println(
            (!teamRepo.findById("test").getMembers().contains(user2) && !user2.hasTeam()) ?
                ":) Success Test: Team name already used" : ":( Failed Test: Team name already used"
        );
    }

    private static void succesTeamCreationTest() {
        // ARRANGE
        TeamRepository teamRepo = new JavaTeamRepository();
        UserRepository userRepo = new JavaUserRepository();
        InvitationRepository invitationRepo = new JavaInvitationRepository();
        CreateTeamHandler handler = new CreateTeamHandlerImpl(
            userRepo, 
            teamRepo, 
            new InvitationService(userRepo, invitationRepo)
        );
        User user = new User("userTest", "userTest@test.test");
        User user2 = new User("user2", "user2@test.test");
        Team team = new Team("test", user);
        user.setTeam(team);
        teamRepo.save(team);
        userRepo.save(user);
        userRepo.save(user2);

        // ACT
        try {
            handler.createTeam(user2.getEmail(), "test2", null);
        } catch (Exception e) {
        }

        // ASSERT
        System.out.println(
            (teamRepo.findById("test2").getMembers().contains(user2) && user2.hasTeam()) ?
                ":) Success Test: Success team creation" : ":( Failed Test: Success team creation"
        );
    }
}
