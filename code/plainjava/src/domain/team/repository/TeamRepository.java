package plainjava.src.domain.team.repository;

import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.utils.repository.Find;
import plainjava.src.domain.utils.repository.Save;

public interface TeamRepository extends
        Save<Team>, Find<Team, String> {
}
