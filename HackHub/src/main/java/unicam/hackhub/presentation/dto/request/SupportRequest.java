package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import unicam.hackhub.domain.utils.TimeRange;

import java.time.LocalDate;

public record SupportRequest(
        @Positive
        Long hackathonId,
        String mentorEmail,
        TimeRange slot,
        @FutureOrPresent
        LocalDate date
) {
}
