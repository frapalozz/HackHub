package unicam.hackhub.presentation.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.application.hackathon.dto.request.CreateHackathonRequest;
import unicam.hackhub.presentation.dto.request.HackathonRequest;

@Component
public class HackathonMapper {

    public CreateHackathonRequest toCreateHackathonRequest(HackathonRequest request) {
            return new CreateHackathonRequest(
                    request.name(),
                    request.subscriptionDeadline(),
                    request.hackathonPeriod(),
                    request.maxTeamSize(),
                    request.requirements(),
                    request.prize(),
                    request.organizerEmail(),
                    request.judgeEmail(),
                    request.mentorsEmails()
            );
    }
}
