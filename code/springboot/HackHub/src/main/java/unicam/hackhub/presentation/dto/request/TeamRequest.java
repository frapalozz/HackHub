package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record TeamRequest(

        @NotBlank(message = "Team name è obbligatorio")
        String teamName,

        List<String> emails) {
}
