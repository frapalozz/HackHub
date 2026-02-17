package unicam.hackhub.application.report;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Report;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.hackathon.repository.ReportRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;

@Service
@Primary
@AllArgsConstructor
public class ReportHandlerImpl implements ReportHandler {

    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;
    private final ReportRepository reportRepository;

    @Override
    public String report(String mentorEmail, String teamName, Long hackathonId, String description) {

        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElse(null);
        Team team = teamRepository.findById(teamName).orElse(null);

        if(hackathon == null | team == null) {
            throw new IllegalArgumentException("Hackathon or Team not found");
        }

        if(!hackathon.inProgress()) {
            throw new IllegalArgumentException("Hackathon not in progress");
        }

        if(!hackathon.hasTeam(team)) {
            throw new IllegalArgumentException("Team not in hackathon");
        }

        if(hackathon.getMentors().stream().noneMatch(m -> m.getEmail().equals(mentorEmail))) {
            throw new IllegalArgumentException("Mentor not in hackathon");
        }

        Report report = Report.builder()
                .team(team)
                .hackathon(hackathon)
                .description(description)
                .build();

        reportRepository.save(report);

        return "Report generated";
    }
}
