package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.support.model.RequestState;
import unicam.hackhub.domain.support.model.SupportRequest;
import unicam.hackhub.domain.support.repository.SupportRequestRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaSupportRequestRepository extends JpaRepository<SupportRequest, Long>, SupportRequestRepository {

    @Override
    default List<SupportRequest> findAllBlockingRequests(String mentorEmail, LocalDate date) {
        return findByMentorEmailAndDateAndStateIn(
                mentorEmail,
                date,
                List.of(RequestState.PENDING, RequestState.SCHEDULED)
        );
    }

    @Query("SELECT sr " +
           "FROM SupportRequest sr " +
           "WHERE sr.mentor.email = :mentorEmail " +
           "AND sr.date = :date " +
           "AND sr.state IN :states")
    List<SupportRequest> findByMentorEmailAndDateAndStateIn(
            @Param("mentorEmail") String mentorEmail,
            @Param("date") LocalDate date,
            @Param("states") List<RequestState> states
    );

    @Override
    @Query("SELECT sr FROM SupportRequest sr WHERE sr.mentor.email = :email")
    List<SupportRequest> findAllWhereIsStaff(@Param("email") String staffEmail);
}
