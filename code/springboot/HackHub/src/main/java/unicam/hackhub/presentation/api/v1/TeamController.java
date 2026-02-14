package unicam.hackhub.presentation.api.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.hackathon.SubmissionHandler;
import unicam.hackhub.application.invitation.InvitationHandler;
import unicam.hackhub.application.team.RegisterTeamHandler;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.presentation.dto.request.SubmissionRequest;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/team")
public class TeamController {

    private final InvitationHandler invitationHandler;
    private final SubmissionHandler submissionHandler;
    private final RegisterTeamHandler registerTeamHandler;

    @PreAuthorize("hasRole('TEAM_MEMBER')")
    @RequestMapping(value = "/{teamName}/invite", method = RequestMethod.POST)
    public ResponseEntity<Object> inviteUser(
            @RequestParam
            @NotBlank(message = "UserEmail è obbligatorio")
            String userEmail,
            @PathVariable
            @NotBlank(message = "Team name è obbligatorio")
            String teamName
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(invitationHandler.inviteUser(userEmail, teamName));
    }

    @PreAuthorize("hasRole('TEAM_MEMBER')")
    @RequestMapping(value = "/register/{hackathonId}", method = RequestMethod.POST)
    public ResponseEntity<Object> registerTeam(
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
                .body(registerTeamHandler.registerTeam(teamName, hackathonId));
    }

    @PreAuthorize("hasRole('TEAM_MEMBER')")
    @RequestMapping(value = "/hackathon/{hackathonId}", method = RequestMethod.POST)
    public ResponseEntity<Object> addSubmission(
            @PathVariable
            @NotNull(message = "Hackathon ID è obbligatorio")
            @Positive(message = "Hackathon ID deve essere positivo")
            Long hackathonId,
            @Validated @RequestBody SubmissionRequest request,
            @RequestParam
            @NotBlank(message = "Team name è obbligatorio")
            String teamName
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(submissionHandler.addSubmission(teamName, hackathonId, new Submission(request.url())));
    }

    @PreAuthorize("hasRole('TEAM_MEMBER')")
    @RequestMapping(value = "/hackathon/{hackathonId}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateSubmission(
            @PathVariable
            @NotNull(message = "Hackathon ID è obbligatorio")
            @Positive(message = "Hackathon ID deve essere positivo")
            Long hackathonId,
            @Validated @RequestBody SubmissionRequest request,
            @RequestParam
            @NotBlank(message = "Team name è obbligatorio")
            String teamName
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(submissionHandler.updateSubmission(teamName, hackathonId, new Submission(request.url())));
    }
}
