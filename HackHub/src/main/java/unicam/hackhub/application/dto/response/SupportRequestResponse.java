package unicam.hackhub.application.dto.response;

import unicam.hackhub.domain.utils.TimeRange;

import java.time.LocalDate;

public record SupportRequestResponse(
        Long id,
        String team,
        Long hackathonId,
        String mentorEmail,
        String state,
        LocalDate date,
        TimeRange slot,
        String linkCall
) {
}
