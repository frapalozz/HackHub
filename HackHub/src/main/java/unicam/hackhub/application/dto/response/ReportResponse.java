package unicam.hackhub.application.dto.response;

public record ReportResponse(
    Long reportId,
    String team,
    Long hackathonId,
    String hackathonName,
    String description
) {
}
