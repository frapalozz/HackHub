package unicam.hackhub.presentation.dto.response;

import java.time.LocalDate;

public record PresentationInvitationResponse(
        LocalDate date,
        String teamName,
        String invitedUser
) {
}
