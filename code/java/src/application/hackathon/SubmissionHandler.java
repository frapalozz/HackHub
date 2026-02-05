package code.java.src.application.hackathon;

import code.java.src.domain.hackathon.model.Submission;

public interface SubmissionHandler {
    
    void addSubmission(String teamName, long hackathonId, Submission submission);
    void updateSubmission(String teamName, long hackathonId, Submission submission);
}
