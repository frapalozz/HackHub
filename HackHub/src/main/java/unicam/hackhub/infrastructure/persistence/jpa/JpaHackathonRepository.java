package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.state.HackathonStatus;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.team.model.Team;

import java.util.List;

@Repository
public interface JpaHackathonRepository extends HackathonRepository, JpaRepository<Hackathon, Long> {

    @Override
    default boolean inActiveHackathon(Team team) {
        return inActiveHackathonInternal(team, HackathonStatus.HackathonStateType.ENDED);
    }

    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END " +
            "FROM Hackathon h " +
            "JOIN h.teams t " +
            "WHERE t = :team " +
            "AND h.status.currentState <> :ended")
    boolean inActiveHackathonInternal(@Param("team") Team team,
                                      @Param("ended") HackathonStatus.HackathonStateType ended);

    @Override
    default List<Hackathon> findPublicHackathons() {
        return findByStates(List.of(
                HackathonStatus.HackathonStateType.SUBSCRIPTION,
                HackathonStatus.HackathonStateType.PROGRESS,
                HackathonStatus.HackathonStateType.EVALUATION
        ));
    }

    @Query("SELECT h FROM Hackathon h WHERE h.status.currentState IN :states")
    List<Hackathon> findByStates(@Param("states") List<HackathonStatus.HackathonStateType> states);

    @Override
    @Query("SELECT h " +
           "FROM Hackathon h " +
           "JOIN h.teams t WHERE t.name = :teamName")
    List<Hackathon> findAllByParticipatingTeam(@Param("teamName") String teamName);

    @Override
    @Query("SELECT DISTINCT h FROM Hackathon h LEFT JOIN h.mentors m " +
            "WHERE h.organizer.email = :email OR h.judge.email = :email OR m.email = :email")
    List<Hackathon> findAllWhereIsStaff(@Param("email") String staffEmail);
}
