package unicam.hackhub.application.dto.response;

import unicam.hackhub.domain.hackathon.model.state.HackathonStatus;
import unicam.hackhub.domain.utils.Period;

import java.time.LocalDate;
import java.util.List;

public record AssignedHackathonResponse(
        Long id,
        String name,
        LocalDate subscriptionDeadline,
        Period hackathonPeriod,
        HackathonStatus.HackathonStateType state,
        int maxTeamSize,
        String requirements,
        Double prize,
        String location,
        String organizer,
        String judge,
        List<String> mentors,
        List<String> teams,
        String teamWinner,
        String myRole
) {
}
