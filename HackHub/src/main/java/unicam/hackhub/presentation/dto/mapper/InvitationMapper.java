package unicam.hackhub.presentation.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.application.dto.response.InvitationResponse;
import unicam.hackhub.presentation.dto.response.PresentationInvitationResponse;

@Component
public class InvitationMapper {

    public PresentationInvitationResponse applicationToPresentationInvitationResponse(InvitationResponse invitation) {
        return new PresentationInvitationResponse(
                invitation.date(),
                invitation.teamName(),
                invitation.receiver().getEmail()
        );
    }
}
