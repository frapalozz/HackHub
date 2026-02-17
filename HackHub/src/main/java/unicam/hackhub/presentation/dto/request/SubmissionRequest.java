package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SubmissionRequest(
        @NotBlank(message = "url non può essere vuoto")
        String url) {
}
