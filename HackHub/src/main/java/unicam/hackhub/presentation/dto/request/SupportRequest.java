package unicam.hackhub.presentation.dto.request;

import unicam.hackhub.domain.utils.TimeRange;

import java.time.LocalDate;

public record SupportRequest(
        Long hackathonId,
        String mentorEmail,
        TimeRange slot,
        LocalDate date
) {
}
