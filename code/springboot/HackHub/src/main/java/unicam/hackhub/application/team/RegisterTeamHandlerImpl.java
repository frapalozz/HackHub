package unicam.hackhub.application.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;

@Service
@Primary
public class RegisterTeamHandlerImpl implements RegisterTeamHandler {

    private final HackathonRepository hackathonRepo;
    private final TeamRepository teamRepo;

    @Autowired
    public RegisterTeamHandlerImpl(HackathonRepository hr, TeamRepository tr) {
        this.hackathonRepo = hr;
        this.teamRepo = tr;
    }

    @Override
    public String registerTeam(String teamName, Long hackathonId) {
        
        Team team = teamRepo.findById(teamName).orElse(null);
        Hackathon hackathon = hackathonRepo.findById(hackathonId).orElse(null);

        if(team == null) throw new IllegalArgumentException("Team not found");
        if(hackathon == null) throw new IllegalArgumentException("Hackathon not found");

        hackathon.registerTeam(team);

        hackathonRepo.save(hackathon);

        return "Team registered to hackathon (id: " + hackathon.getId();
    }
    
}
