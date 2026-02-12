package unicam.hackhub.application.hackathon;

import java.util.List;

public interface HackathonHandler {

    String declareWinner(Long hackathonId, String teamName);
    String addMentors(Long hackathonId, List<String> mentorsList);
}
