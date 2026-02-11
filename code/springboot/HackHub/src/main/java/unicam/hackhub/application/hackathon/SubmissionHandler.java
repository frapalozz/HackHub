package unicam.hackhub.application.hackathon;

import unicam.hackhub.domain.hackathon.model.Submission;

public interface SubmissionHandler {
    
    String addSubmission(String teamName, long hackathonId, Submission submission);
    String updateSubmission(String teamName, long hackathonId, Submission submission);
    String valuateSubmission(Long hackathonId, String teamName, int vote, String description);
    String editValuation(Long hackathonId, String teamName, int vote, String description);
}
