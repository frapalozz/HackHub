package unicam.hackhub.presentation.api.v1;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.hackathon.CreateHackathonHandler;
import unicam.hackhub.application.hackathon.HackathonHandler;
import unicam.hackhub.application.hackathon.SubmissionHandler;
import unicam.hackhub.application.report.ReportHandler;
import unicam.hackhub.presentation.dto.request.HackathonRequest;
import unicam.hackhub.presentation.dto.request.ReportRequest;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/staff")
public class StaffController {

    private final CreateHackathonHandler createHackathonHandler;
    private final SubmissionHandler submissionHandler;
    private final HackathonHandler hackathonHandler;
    private final ReportHandler reportHandler;

    @RequestMapping(value = "/hackathon", method = RequestMethod.POST)
    public ResponseEntity<Object> createHackathon(@Validated @RequestBody HackathonRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createHackathonHandler.createHackathon(request.toCreateHackathonRequest()));
    }

    @RequestMapping(value = "/hackathon/{hackathonId}", method = RequestMethod.POST)
    public ResponseEntity<Object> declareWinner(
            @PathVariable
            @NotNull(message = "Hackathon ID è obbligatorio")
            @Positive(message = "Hackathon ID deve essere positivo")
            Long hackathonId,
            @RequestParam
            @NotBlank(message = "Team name è obbligatorio")
            String teamName
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hackathonHandler.declareWinner(hackathonId, teamName));
    }

    @RequestMapping(value = "/hackathon/{hackathonId}/{teamName}", method = RequestMethod.POST)
    public ResponseEntity<Object> valuateSubmission(
            @PathVariable
            @NotNull(message = "Hackathon ID è obbligatorio")
            @Positive(message = "Hackathon ID deve essere positivo")
            Long hackathonId,
            @PathVariable
            @NotBlank(message = "Team name è obbligatorio")
            String teamName,
            @RequestParam
            @Min(value = 0, message = "Il voto deve essere almeno 0")
            @Max(value = 10, message = "Il voto non può superare 10")
            int vote,
            @RequestParam
            @NotBlank(message = "Il messaggio è obbligatorio")
            @Size(max = 500, message = "Il messaggio non può superare 500 caratteri")
            String message
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(submissionHandler.valuateSubmission(hackathonId, teamName, vote, message));
    }

    @RequestMapping(value = "/hackathon/{hackathonId}/{teamName}", method = RequestMethod.PUT)
    public ResponseEntity<Object> editValuation(
            @PathVariable
            @NotNull(message = "Hackathon ID è obbligatorio")
            @Positive(message = "Hackathon ID deve essere positivo")
            Long hackathonId,
            @PathVariable
            @NotBlank(message = "Team name è obbligatorio")
            String teamName,
            @RequestParam
            @Min(value = 0, message = "Il voto deve essere almeno 0")
            @Max(value = 10, message = "Il voto non può superare 10")
            int vote,
            @RequestParam
            @NotBlank(message = "Il messaggio è obbligatorio")
            @Size(max = 500, message = "Il messaggio non può superare 500 caratteri")
            String message
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(submissionHandler.editValuation(hackathonId, teamName, vote, message));
    }

    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public ResponseEntity<Object> reportTeam(@Validated @RequestBody ReportRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reportHandler.report(request.teamName(), request.hackathonId(), request.description()));
    }
}
