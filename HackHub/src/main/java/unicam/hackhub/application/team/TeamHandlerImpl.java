package unicam.hackhub.application.team;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.application.invitation.InvitationHandler;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;
import unicam.hackhub.domain.utils.Role;

import java.util.List;

@Service
@Primary
@AllArgsConstructor
public class TeamHandlerImpl implements TeamHandler {

    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;

    private final InvitationHandler invitationHandler;

    @Override
    public String createTeam(String userId, String teamName, List<String> invitedUsers) {

        User user = userRepository.findById(userId).orElse(null);

        this.validateTeamCreation(teamName, user);

        Team team = new Team(teamName, user);

        Team teamSaved = teamRepository.save(team);
        user.setTeam(teamSaved);
        user.setRole(Role.TEAM_MEMBER);
        userRepository.save(user);

        invitationHandler.createInvitations(teamSaved, invitedUsers);

        return "Team Created";
    }

    @Override
    public String registerTeam(String teamMember, Long hackathonId) {

        Team team = getUser(teamMember).getTeam();
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElse(null);

        if(team == null) throw new IllegalArgumentException("Team not found");
        if(hackathon == null) throw new IllegalArgumentException("Hackathon not found");

        hackathon.registerTeam(team);

        hackathonRepository.save(hackathon);

        return "Team registered to hackathon (id: " + hackathon.getId() + ")";
    }

    private void validateTeamCreation(String teamName, User user) {
        if(user == null) throw new IllegalArgumentException("User not found");

        if(user.hasTeam()) throw new IllegalStateException("User already in a team");

        if(teamRepository.findById(teamName).isPresent()) throw new IllegalArgumentException("Team name already used");;
    }

    private User getUser(String email) {
        User user = userRepository.findById(email).orElse(null);

        if(user == null) throw new IllegalArgumentException("User not found");

        return user;
    }
}
