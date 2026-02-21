package unicam.hackhub.application.dto.response;

import java.time.LocalDate;

public record InvitationResponse(
        LocalDate date,
        String teamName,
        String invitedUser
) {
}
