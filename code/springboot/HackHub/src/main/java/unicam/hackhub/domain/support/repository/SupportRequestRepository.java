package unicam.hackhub.domain.support.repository;

import unicam.hackhub.domain.support.model.SupportRequest;
import unicam.hackhub.domain.utils.repository.Save;

import java.time.LocalDate;
import java.util.List;

public interface SupportRequestRepository extends Save<SupportRequest> {

    List<SupportRequest> findAllBlockingRequests(String mentorEmail, LocalDate date);
}
