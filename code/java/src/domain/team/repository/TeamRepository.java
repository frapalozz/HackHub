package code.java.src.domain.team.repository;

import code.java.src.domain.team.model.Team;

public interface TeamRepository {
    
    Team findById(String teamName);
    void save(Team team);
}
