package unicam.hackhub.application.dto.response;

import unicam.hackhub.domain.user.model.User;

import java.time.LocalDate;

public record InvitationResponse(
        LocalDate date,
        String teamName,
        User receiver) {
}
