package unicam.hackhub.application.hackathon;

import unicam.hackhub.application.dto.response.AssignedHackathonResponse;
import unicam.hackhub.application.dto.response.HackathonResponse;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.model.Report;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.domain.support.model.SupportRequest;

import java.util.List;

public interface HackathonViewHandler {

    List<HackathonResponse> getPublicHackathons();
    HackathonResponse getHackathonDetails(Long hackathonId);
    List<Report> getReports(String staffEmail);
    List<SupportRequest> getSupportRequests(String staffEmail);
    List<Hackathon> getAllHackathons();
    List<Submission> getSubmissions(String staffEmail, Long hackathonId);
    List<AssignedHackathonResponse> getAssignedHackathons(String staffEmail);
    Submission getSubmissionStaff(String staffEmail, Long submissionId);
    List<HackathonResponse> getParticipatingHackathons(String user);
    Submission getSubmissionTeam(String user, Long hackathonId);
}
