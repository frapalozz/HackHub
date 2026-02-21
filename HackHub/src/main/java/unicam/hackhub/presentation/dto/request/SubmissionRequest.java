package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SubmissionRequest(
        @NotBlank
        @Pattern(
                regexp = "^(http|https|ftp)://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(:[0-9]{1,5})?(/.*)?$",
                message = "Invalid URL format"
        )
        String url) {
}
