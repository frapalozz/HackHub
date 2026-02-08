package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;

@Repository
public interface JpaHackathonRepository extends HackathonRepository, JpaRepository<Hackathon, Long> {
}
