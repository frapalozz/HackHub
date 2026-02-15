package unicam.hackhub.application.team;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;
import unicam.hackhub.application.invitation.InvitationService;
import unicam.hackhub.domain.utils.Role;

import java.util.List;

 @Service
 @Primary
 @AllArgsConstructor
public class CreateTeamHandlerImpl implements CreateTeamHandler {
    
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    private final InvitationService invitationService;

    @Override
    public String createTeam(String userId, String teamName, List<String> invitedUsers) {
        
        User user = userRepository.findById(userId).orElse(null);

        this.validateTeamCreation(teamName, user);

        Team team = new Team(teamName, user);
        user.setTeam(team);
        user.setRole(Role.TEAM_MEMBER);

        teamRepository.save(team);

        invitationService.createInvitations(team, invitedUsers);

        return "Team Created";
    }

    private void validateTeamCreation(String teamName, User user) {
        if(user == null) throw new IllegalArgumentException("User not found");

        if(user.hasTeam()) throw new IllegalStateException("User already in a team");

        if(teamRepository.findById(teamName).isPresent()) throw new IllegalArgumentException("Team name already used");;
    }
}
