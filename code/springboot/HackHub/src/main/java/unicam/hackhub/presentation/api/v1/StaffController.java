package unicam.hackhub.presentation.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.hackathon.CreateHackathonHandler;
import unicam.hackhub.application.hackathon.HackathonHandler;
import unicam.hackhub.application.hackathon.SubmissionHandler;
import unicam.hackhub.application.hackathon.request.CreateHackathonRequest;

@Validated
@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

    private final CreateHackathonHandler createHackathonHandler;
    private final SubmissionHandler submissionHandler;
    private final HackathonHandler hackathonHandler;

    public StaffController(CreateHackathonHandler createHackathonHandler,
                           SubmissionHandler submissionHandler, HackathonHandler hackathonHandler) {
        this.createHackathonHandler = createHackathonHandler;
        this.submissionHandler = submissionHandler;
        this.hackathonHandler = hackathonHandler;
    }

    @RequestMapping(value = "/hackathon", method = RequestMethod.POST)
    public ResponseEntity<Object> createHackathon(@Validated @RequestBody CreateHackathonRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createHackathonHandler.createHackathon(request));
    }

    @RequestMapping(value = "/hackathon/{hackathonId}", method = RequestMethod.POST)
    public ResponseEntity<Object> declareWinner(@PathVariable Long hackathonId, @RequestParam String teamName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hackathonHandler.declareWinner(hackathonId, teamName));
    }

    @RequestMapping(value = "/hackathon/{hackathonId}/{teamName}", method = RequestMethod.POST)
    public ResponseEntity<Object> valuateSubmission(@PathVariable Long hackathonId,
                                                    @PathVariable String teamName,
                                                    @RequestParam int vote,
                                                    @RequestParam String message) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(submissionHandler.valuateSubmission(hackathonId, teamName, vote, message));
    }
}
