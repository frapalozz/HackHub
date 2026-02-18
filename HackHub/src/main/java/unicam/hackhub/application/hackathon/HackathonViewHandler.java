package unicam.hackhub.application.hackathon;

import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Report;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.support.model.SupportRequest;

import java.util.List;

public interface HackathonViewHandler {

    List<Hackathon> getHackathons();
    Hackathon getHackathonDetails(Long hackathonId);
    List<Report> getReports(String staffEmail);
    List<SupportRequest> getSupportRequests(String staffEmail);
    List<Hackathon> getAllHackathons();
    List<Submission> getSubmissions(String staffEmail, Long hackathonId);
    List<Hackathon> getAssignedHackathons(String staffEmail);
    Submission getSubmissionStaff(String staffEmail, Long submissionId);
    List<Hackathon> getParticipatingHackathons(String user);
    Submission getSubmissionTeam(String user, Long hackathonId);
}
