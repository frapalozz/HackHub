package code.java.domain.hackathon.repository;

import code.java.domain.hackathon.model.Hackathon;

public interface HackathonRepository {
    
    Hackathon findById(Long id);

    void save(Hackathon hackathon);
}
