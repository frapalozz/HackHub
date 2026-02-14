package unicam.hackhub.application.auth;

import jakarta.validation.constraints.NotBlank;
import unicam.hackhub.application.dto.response.TokenResponse;

public interface AuthHandler {

    TokenResponse login(String email, String password, String type);

}
