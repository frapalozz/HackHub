package code.java.src.infrastructure.persistence.plainJava;

import java.util.Set;

import code.java.src.domain.team.model.Team;
import code.java.src.domain.team.repository.TeamRepository;

public class JavaTeamRepository implements TeamRepository {

    private Set<Team> teams;

    @Override
    public Team findById(String teamName) {
        return this.teams.stream()
            .filter(t -> t.getName() == teamName)
            .findFirst()
            .orElse(null);
    }

    @Override
    public void save(Team team) {
        Team teamPresent = this.findById(team.getName());

        if(teamPresent == null) {
            this.teams.add(team);
        } else {
            this.teams.remove(teamPresent);
            this.teams.add(team);
        }
    }
    
}
