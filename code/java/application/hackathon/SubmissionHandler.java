package code.java.application.hackathon;

import code.java.domain.hackathon.model.Submission;

public interface SubmissionHandler {
    
    void addSubmission(String teamName, long hackathonId, Submission submission);
    void updateSubmission(String teamName, long hackathonId, Submission submission);
}
