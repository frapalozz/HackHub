package code.java.src.domain.hackathon.repository;

import code.java.src.domain.hackathon.model.Hackathon;

public interface HackathonRepository {
    
    Hackathon findById(Long id);

    void save(Hackathon hackathon);
}
