package plainjava.src.application.team;

import java.util.List;

import plainjava.src.application.invitation.InvitationService;
import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.team.repository.TeamRepository;
import plainjava.src.domain.user.model.User;
import plainjava.src.domain.user.repository.UserRepository;

public class CreateTeamHandlerImpl implements CreateTeamHandler {
    
    private UserRepository userRepository;
    private TeamRepository teamRepository;

    private InvitationService invitationService;

    public CreateTeamHandlerImpl(UserRepository ur, TeamRepository tr, InvitationService is) {
        this.userRepository = ur;
        this.teamRepository = tr;
        this.invitationService = is;
    }

    @Override
    public String createTeam(String userId, String teamName, List<String> invitedUsers) {
        
        User user = userRepository.findById(userId);

        this.validateTeamCreation(teamName, user);

        Team team = new Team(teamName, user);
        user.setTeam(team);

        teamRepository.save(team);

        invitationService.createInvitations(team, invitedUsers);

        return "Team Created";
    }

    private void validateTeamCreation(String teamName, User user) {
        if(user == null) throw new IllegalArgumentException("User not found");

        if(user.hasTeam()) throw new IllegalStateException("User already in a team");

        if(teamRepository.findById(teamName) != null) throw new IllegalArgumentException("Team name already used");;
    }
}
