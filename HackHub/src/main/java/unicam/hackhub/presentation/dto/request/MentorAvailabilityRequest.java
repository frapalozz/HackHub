package unicam.hackhub.presentation.dto.request;

import java.time.LocalDate;

public record MentorAvailabilityRequest(
        String mentorEmail,
        LocalDate date
) {
}
