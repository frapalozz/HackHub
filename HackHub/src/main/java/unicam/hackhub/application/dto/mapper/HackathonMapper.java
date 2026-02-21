package unicam.hackhub.application.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.application.dto.response.AssignedHackathonResponse;
import unicam.hackhub.application.dto.response.HackathonResponse;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.team.model.Team;

@Component("presentationHackathonMapper")
public class HackathonMapper {

    public HackathonResponse hackathonToHackathonResponse(Hackathon hackathon) {
        return new HackathonResponse(
                hackathon.getId(),
                hackathon.getName(),
                hackathon.getSubscriptionDeadline(),
                hackathon.getHackathonPeriod(),
                hackathon.getStatus().getCurrentState(),
                hackathon.getMaxTeamSize(),
                hackathon.getRequirements(),
                hackathon.getPrize(),
                hackathon.getLocation(),
                hackathon.getOrganizer().getEmail(),
                hackathon.getJudge().getEmail(),
                hackathon.getMentors().stream().map(Staff::getEmail).toList(),
                hackathon.getTeams().stream().map(Team::getName).toList(),
                hackathon.getWinner() == null ? null : hackathon.getWinner().getName()
        );
    }

    public AssignedHackathonResponse hackathonToAssignedHackathon(Hackathon hackathon, String staffEmail) {
        return new AssignedHackathonResponse(
                hackathon.getId(),
                hackathon.getName(),
                hackathon.getSubscriptionDeadline(),
                hackathon.getHackathonPeriod(),
                hackathon.getStatus().getCurrentState(),
                hackathon.getMaxTeamSize(),
                hackathon.getRequirements(),
                hackathon.getPrize(),
                hackathon.getLocation(),
                hackathon.getOrganizer().getEmail(),
                hackathon.getJudge().getEmail(),
                hackathon.getMentors().stream().map(Staff::getEmail).toList(),
                hackathon.getTeams().stream().map(Team::getName).toList(),
                hackathon.getWinner() == null ? null : hackathon.getWinner().getName(),
                getStaffRole(hackathon, staffEmail)
        );
    }

    private String getStaffRole(Hackathon hackathon, String staffEmail) {
        if(hackathon.getOrganizer().getEmail().equals(staffEmail)) {
            return "ORGANIZER";
        }
        if(hackathon.getJudge().getEmail().equals(staffEmail)) {
            return "JUDGE";
        }
        if(hackathon.getMentors().stream().anyMatch(m -> m.getEmail().equals(staffEmail))) {
            return "MENTOR";
        }

        throw new IllegalArgumentException("Staff not in hackathon: " + hackathon.getId());
    }
}
