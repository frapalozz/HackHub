package unicam.hackhub.domain.hackathon.repository;

import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.utils.repository.Find;
import unicam.hackhub.domain.utils.repository.FindWhereIsStaff;
import unicam.hackhub.domain.utils.repository.Save;

import java.util.List;

public interface HackathonRepository extends
        Find<Hackathon, Long>, Save<Hackathon>, FindWhereIsStaff<Hackathon> {

    boolean inActiveHackathon(Team team);
    List<Hackathon> findPublicHackathons();
    List<Hackathon> findAllByParticipatingTeam(String teamName);
    Hackathon findHackathonOfSubmission(Long submissionId);
}
