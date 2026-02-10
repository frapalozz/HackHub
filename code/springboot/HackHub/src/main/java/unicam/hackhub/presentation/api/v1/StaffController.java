package unicam.hackhub.presentation.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
}
