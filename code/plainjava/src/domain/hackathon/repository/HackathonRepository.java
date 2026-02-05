package plainjava.src.domain.hackathon.repository;

import plainjava.src.domain.hackathon.model.Hackathon;
import plainjava.src.domain.utils.repository.Find;
import plainjava.src.domain.utils.repository.Save;

public interface HackathonRepository extends
        Find<Hackathon, Long>, Save<Hackathon> {
}
