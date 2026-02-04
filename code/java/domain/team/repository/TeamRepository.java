package code.java.domain.team.repository;

import code.java.domain.team.model.Team;

public interface TeamRepository {
    
    Team findById(String teamName);
    void save(Team team);
}
