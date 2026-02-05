package code.java.src.application.team;

import java.util.List;

public interface CreateTeamHandler {
    
    String createTeam(String userId, String teamName, List<String> invitedUsers);
}
