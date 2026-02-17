package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ReportRequest(
        @NotBlank(message = "Team name è obbligatorio")
        String teamName,

        @Positive(message = "hackathonId deve essere positivo")
        Long hackathonId,

        @NotBlank(message = "Description è obbligatoria")
        String description
) {
}
