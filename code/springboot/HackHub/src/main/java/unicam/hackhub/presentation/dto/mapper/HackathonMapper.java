package unicam.hackhub.presentation.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.application.dto.request.CreateHackathonRequest;
import unicam.hackhub.domain.utils.Period;
import unicam.hackhub.presentation.dto.request.HackathonRequest;

@Component
public class HackathonMapper {

    public CreateHackathonRequest toCreateHackathonRequest(HackathonRequest request) {
            return new CreateHackathonRequest(
                    request.name(),
                    request.subscriptionDeadline(),
                    new Period(request.startDate(), request.endDate()),
                    request.maxTeamSize(),
                    request.requirements(),
                    request.prize(),
                    request.organizerEmail(),
                    request.judgeEmail(),
                    request.mentorsEmails()
            );
    }
}
