package unicam.hackhub.presentation.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.application.dto.command.CreateHackathonCommand;
import unicam.hackhub.domain.utils.Period;
import unicam.hackhub.presentation.dto.request.HackathonRequest;

@Component
public class HackathonMapper {

    public CreateHackathonCommand toCreateHackathonRequest(HackathonRequest request, String organizerEmail) {
            return new CreateHackathonCommand(
                    request.name(),
                    request.location(),
                    request.subscriptionDeadline(),
                    new Period(request.startDate(), request.endDate()),
                    request.maxTeamSize(),
                    request.requirements(),
                    request.prize(),
                    organizerEmail,
                    request.judgeEmail(),
                    request.mentorsEmails()
            );
    }
}
