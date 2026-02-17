package unicam.hackhub.domain.hackathon.repository;

import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.utils.repository.Find;
import unicam.hackhub.domain.utils.repository.Save;

public interface HackathonRepository extends
        Find<Hackathon, Long>, Save<Hackathon> {

    boolean inActiveHackathon(Team team);
}
