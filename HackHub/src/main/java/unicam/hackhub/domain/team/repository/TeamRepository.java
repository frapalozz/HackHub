package unicam.hackhub.domain.team.repository;

import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.utils.repository.Find;
import unicam.hackhub.domain.utils.repository.Save;

public interface TeamRepository extends
        Save<Team>, Find<Team, String> {
}
