package code.java.src.application.hackathon.request;

import java.time.LocalDate;
import java.util.List;

import code.java.src.domain.utils.Period;

public record CreateHackathonRequest(
    String name, 
    LocalDate subscriptionDeadline, 
    Period hackathonPeriod, 
    int maxTeamSize,
    String requirements,
    Double prize,
    String organizerEmail,
    String judgeEmail,
    List<String> mentorsEmails) {
    
}
