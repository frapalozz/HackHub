package plainjava.src.domain.team.repository;

import plainjava.src.domain.team.model.Team;

public interface TeamRepository {
    
    Team findById(String teamName);
    void save(Team team);
}
