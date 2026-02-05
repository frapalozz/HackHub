package plainjava.src.infrastructure.persistence.plainJava;

import java.util.*;

import plainjava.src.domain.hackathon.model.Hackathon;
import plainjava.src.domain.hackathon.repository.HackathonRepository;

public class JavaHackathonRepository implements HackathonRepository {
    
    private final Set<Hackathon> hackathons = new HashSet<>();

    @Override
    public Hackathon findById(Long id) {
        return hackathons.stream()
        .filter(hackathon -> hackathon.getId() == id)
        .findFirst()
        .orElse(null);
    }

    @Override
    public List<Hackathon> findAll(List<Long> ids) {
        return hackathons.stream()
                .filter(h -> ids.contains(h.getId()))
                .toList();
    }

    @Override
    public Hackathon save(Hackathon hackathon) {
        if(hackathon == null) return null;
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

        return hackathon;
    }

    @Override
    public void saveAll(List<Hackathon> entities) {
        entities.forEach(this::save);
    }
}
