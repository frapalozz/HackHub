package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "userid è obbligatorio")
        String userId,
        String email,
        String name
) {
}
