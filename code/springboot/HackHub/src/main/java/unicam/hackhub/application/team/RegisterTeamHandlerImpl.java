package unicam.hackhub.application.team;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;

@Service
@Primary
@AllArgsConstructor
public class RegisterTeamHandlerImpl implements RegisterTeamHandler {

    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;

    @Override
    public String registerTeam(String teamMember, Long hackathonId) {

        Team team = getUser(teamMember).getTeam();
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElse(null);

        if(team == null) throw new IllegalArgumentException("Team not found");
        if(hackathon == null) throw new IllegalArgumentException("Hackathon not found");

        hackathon.registerTeam(team);

        hackathonRepository.save(hackathon);

        return "Team registered to hackathon (id: " + hackathon.getId();
    }

    private User getUser(String email) {
        User user = userRepository.findById(email).orElse(null);

        if(user == null) throw new IllegalArgumentException("User not found");

        return user;
    }
    
}
