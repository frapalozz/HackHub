package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unicam.hackhub.domain.hackathon.model.Report;
import unicam.hackhub.domain.hackathon.repository.ReportRepository;

import java.util.List;

public interface JpaReportRepository extends JpaRepository<Report, Long>, ReportRepository {

    @Override
    @Query("SELECT DISTINCT r FROM Report r JOIN r.hackathon h LEFT JOIN h.mentors m " +
            "WHERE h.organizer.email = :email OR h.judge.email = :email OR m.email = :email")
    List<Report> findAllWhereIsStaff(@Param("email") String staffEmail);
}
