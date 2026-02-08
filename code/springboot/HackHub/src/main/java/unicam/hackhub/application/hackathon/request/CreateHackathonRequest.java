package unicam.hackhub.application.hackathon.request;

import unicam.hackhub.domain.utils.Period;

import java.time.LocalDate;
import java.util.List;

/**
 * Request to create a new Hackathon
 * @param name hackathon name
 * @param subscriptionDeadline subscription deadline
 * @param hackathonPeriod hackathon period
 * @param maxTeamSize max team size
 * @param requirements hackathon requirements
 * @param prize prize
 * @param organizerEmail email of the creator of the hackathon
 * @param judgeEmail email of the judge of the hackathon
 * @param mentorsEmails emails of the mentors of the hackathon
 */
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
