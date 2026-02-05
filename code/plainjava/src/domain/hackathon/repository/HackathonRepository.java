package plainjava.src.domain.hackathon.repository;

import plainjava.src.domain.hackathon.model.Hackathon;

public interface HackathonRepository {
    
    Hackathon findById(Long id);

    void save(Hackathon hackathon);
}
