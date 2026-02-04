package code.java.application.team;

import java.util.List;

public interface CreateTeamHandler {
    
    void createTeam(String userId, String teamName, List<String> invitedUsers);
}
