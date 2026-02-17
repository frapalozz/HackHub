package unicam.hackhub.domain.hackathon.repository;

import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.utils.repository.Find;
import unicam.hackhub.domain.utils.repository.Save;

public interface SubmissionRepository extends Find<Submission, Long>, Save<Submission> {
}
