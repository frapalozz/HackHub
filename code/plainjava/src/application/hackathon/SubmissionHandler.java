package plainjava.src.application.hackathon;

import plainjava.src.domain.hackathon.model.Submission;

public interface SubmissionHandler {
    
    String addSubmission(String teamName, long hackathonId, Submission submission);
    String updateSubmission(String teamName, long hackathonId, Submission submission);
}
