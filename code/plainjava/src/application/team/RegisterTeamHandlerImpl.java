package plainjava.src.application.team;

import plainjava.src.domain.hackathon.model.Hackathon;
import plainjava.src.domain.hackathon.repository.HackathonRepository;
import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.team.repository.TeamRepository;

public class RegisterTeamHandlerImpl implements RegisterTeamHandler {

    private HackathonRepository hackathonRepo;
    private TeamRepository teamRepo;

    public RegisterTeamHandlerImpl(HackathonRepository hr, TeamRepository tr) {
        this.hackathonRepo = hr;
        this.teamRepo = tr;
    }

    @Override
    public String registerTeam(String teamName, Long hackathonId) {
        
        Team team = teamRepo.findById(teamName);
        Hackathon hackathon = hackathonRepo.findById(hackathonId);

        if(team == null) throw new IllegalArgumentException("Team not found");
        if(hackathon == null) throw new IllegalArgumentException("Hackathon not found");

        hackathon.registerTeam(team);

        hackathonRepo.save(hackathon);

        return "Team registered to hackathon (id: " + hackathon.getId();
    }
    
}
