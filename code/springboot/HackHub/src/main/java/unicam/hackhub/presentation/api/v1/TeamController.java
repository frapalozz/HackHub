package unicam.hackhub.presentation.api.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.hackathon.SubmissionHandler;
import unicam.hackhub.application.hackathon.request.CreateHackathonRequest;
import unicam.hackhub.application.invitation.InvitationHandler;
import unicam.hackhub.application.team.RegisterTeamHandler;
import unicam.hackhub.domain.hackathon.model.Submission;

@Validated
@RestController
@RequestMapping("/api/v1/team")
public class TeamController {

    private final InvitationHandler invitationHandler;
    private final SubmissionHandler submissionHandler;
    private final RegisterTeamHandler registerTeamHandler;

    public TeamController(InvitationHandler invitationHandler, SubmissionHandler submissionHandler, RegisterTeamHandler registerTeamHandler) {
        this.invitationHandler = invitationHandler;
        this.submissionHandler = submissionHandler;
        this.registerTeamHandler = registerTeamHandler;
    }

    @RequestMapping(value = "/{teamName}/invite", method = RequestMethod.POST)
    public ResponseEntity<Object> inviteUser(@RequestParam String userEmail, @PathVariable String teamName) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(invitationHandler.inviteUser(userEmail, teamName));
    }

    @RequestMapping(value = "/register/{hackathonId}", method = RequestMethod.POST)
    public ResponseEntity<Object> registerTeam(@PathVariable Long hackathonId, @RequestParam String teamName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(registerTeamHandler.registerTeam(teamName, hackathonId));
    }

    @RequestMapping(value = "/hackathon/{hackathonId}", method = RequestMethod.POST)
    public ResponseEntity<Object> addSubmission(@PathVariable Long hackathonId,
                                                @RequestBody Submission submission,
                                                @RequestParam String teamName) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(submissionHandler.addSubmission(teamName, hackathonId, submission));
    }

    @RequestMapping(value = "/hackathon/{hackathonId}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateSubmission(@PathVariable Long hackathonId,
                                                @RequestBody Submission submission,
                                                @RequestParam String teamName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(submissionHandler.updateSubmission(teamName, hackathonId, submission));
    }
}
