package code.java.src.application.team;

import java.util.List;

import code.java.src.application.invitation.InvitationService;
import code.java.src.domain.team.model.Team;
import code.java.src.domain.team.repository.TeamRepository;
import code.java.src.domain.user.model.User;
import code.java.src.domain.user.repository.UserRepository;

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
