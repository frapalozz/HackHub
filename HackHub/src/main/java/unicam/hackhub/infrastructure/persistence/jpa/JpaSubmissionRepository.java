package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.hackathon.repository.SubmissionRepository;

public interface JpaSubmissionRepository extends JpaRepository<Submission, Long>, SubmissionRepository {
}
