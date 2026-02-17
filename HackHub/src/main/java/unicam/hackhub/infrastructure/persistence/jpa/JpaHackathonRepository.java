package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.team.model.Team;

@Repository
public interface JpaHackathonRepository extends HackathonRepository, JpaRepository<Hackathon, Long> {

    @Override
    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END " +
            "FROM Hackathon h " +
            "JOIN h.teams t " +
            "WHERE t = :team " +
            "AND h.status.currentState <> 'ENDED'")
    boolean inActiveHackathon(@Param("team") Team team);
}
