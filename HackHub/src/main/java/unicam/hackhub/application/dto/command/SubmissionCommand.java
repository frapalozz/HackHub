package unicam.hackhub.application.dto.command;

public record SubmissionCommand(
        String userEmail,
        Long hackathonId,
        String url
) {
}
