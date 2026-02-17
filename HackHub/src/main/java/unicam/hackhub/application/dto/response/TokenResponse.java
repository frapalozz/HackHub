package unicam.hackhub.application.dto.response;

public record TokenResponse(
        String accessToken,
        String tokenType,
        String expiration) {
}
