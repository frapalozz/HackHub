package code.java.infrastructure.persistence.plainJava;

import java.util.Set;

import code.java.domain.hackathon.model.Hackathon;
import code.java.domain.hackathon.repository.HackathonRepository;

public class JavaHackathonRepository implements HackathonRepository {
    
    private Set<Hackathon> hackathons;

    @Override
    public Hackathon findById(Long id) {
        return hackathons.stream()
        .filter(hackathon -> hackathon.getId() == id)
        .findFirst()
        .orElse(null);
    }

    @Override
    public void save(Hackathon hackathon) {
        Hackathon hackathonPresent = this.findById(hackathon.getId());

        if(hackathonPresent == null) {
            this.hackathons.add(hackathon);
        } else {
            this.hackathons.remove(hackathonPresent);
            this.hackathons.add(hackathon);
        }
    }
}
