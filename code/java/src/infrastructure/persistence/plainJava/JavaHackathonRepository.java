package code.java.src.infrastructure.persistence.plainJava;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import code.java.src.domain.hackathon.model.Hackathon;
import code.java.src.domain.hackathon.repository.HackathonRepository;

public class JavaHackathonRepository implements HackathonRepository {
    
    private Set<Hackathon> hackathons = new HashSet<>();

    @Override
    public Hackathon findById(Long id) {
        return hackathons.stream()
        .filter(hackathon -> hackathon.getId() == id)
        .findFirst()
        .orElse(null);
    }

    @Override
    public void save(Hackathon hackathon) {
        if(hackathon == null) return;
        Hackathon hackathonPresent = this.findById(hackathon.getId());

        if(hackathonPresent == null) {
            Optional<Hackathon> hackathonMaxId = hackathons.stream().max(Comparator.comparingLong(Hackathon::getId));
            if(hackathonMaxId.isPresent()) hackathon.setId(hackathonMaxId.get().getId()+1);
            else hackathon.setId(1L);
            this.hackathons.add(hackathon);
        } else {
            this.hackathons.remove(hackathonPresent);
            this.hackathons.add(hackathon);
        }
    }
}
