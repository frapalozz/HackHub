package unicam.hackhub.presentation.dto.response;

public record ReportResponse(
        Long id,
        String team,
        Long hackathonId,
        String hackathonName,
        String description
) {
}
