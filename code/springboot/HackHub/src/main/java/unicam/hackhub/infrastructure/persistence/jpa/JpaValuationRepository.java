package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import unicam.hackhub.domain.hackathon.model.Valuation;
import unicam.hackhub.domain.hackathon.repository.ValuationRepository;

public interface JpaValuationRepository extends JpaRepository<Valuation, Long>, ValuationRepository {
}
