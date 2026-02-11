package unicam.hackhub.application.team;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;

@Service
@Primary
@AllArgsConstructor
public class RegisterTeamHandlerImpl implements RegisterTeamHandler {

    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;

    @Override
    public String registerTeam(String teamName, Long hackathonId) {
        System.out.println("Registering team " + teamName);
        Team team = teamRepository.findById(teamName).orElse(null);
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElse(null);

        if(team == null) throw new IllegalArgumentException("Team not found");
        if(hackathon == null) throw new IllegalArgumentException("Hackathon not found");

        hackathon.registerTeam(team);

        hackathonRepository.save(hackathon);

        return "Team registered to hackathon (id: " + hackathon.getId();
    }
    
}
