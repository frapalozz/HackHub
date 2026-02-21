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

import java.util.List;

@Service
@Primary
@AllArgsConstructor
public class ReportHandlerImpl implements ReportHandler {

    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;
    private final ReportRepository reportRepository;

    @Override
    public String report(String mentorEmail, String teamName, Long hackathonId, String description) {

        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon id not found"));
        Team team = teamRepository.findById(teamName)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        reportRepository.save(hackathon.buildReport(mentorEmail, team, description));

        return "Report generated";
    }

    @Override
    public List<Report> getReports(String staffEmail) {

        return reportRepository.findAllWhereIsStaff(staffEmail);
    }
}
