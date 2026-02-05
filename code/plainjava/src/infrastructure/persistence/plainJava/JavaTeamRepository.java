package plainjava.src.infrastructure.persistence.plainJava;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.team.repository.TeamRepository;

public class JavaTeamRepository implements TeamRepository {

    private final Set<Team> teams = new HashSet<>();

    @Override
    public Team findById(String teamName) {
        return this.teams.stream()
            .filter(t -> t.getName() == teamName)
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Team> findAll(List<String> ids) {
        return teams.stream()
                .filter(t -> ids.contains(t.getName()))
                .toList();
    }

    @Override
    public Team save(Team team) {
        Team teamPresent = this.findById(team.getName());

        if(teamPresent == null) {
            this.teams.add(team);
        } else {
            this.teams.remove(teamPresent);
            this.teams.add(team);
        }

        return team;
    }

    @Override
    public void saveAll(List<Team> entities) {
        entities.forEach(this::save);
    }

}
