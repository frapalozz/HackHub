package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ValuateSubmissionRequest(
        @Min(value = 0, message = "Il voto deve essere almeno 0")
        @Max(value = 10, message = "Il voto non può superare 10")
        int vote,

        @NotBlank(message = "Il messaggio è obbligatorio")
        @Size(max = 500, message = "Il messaggio non può superare 500 caratteri")
        String message
) {
}
