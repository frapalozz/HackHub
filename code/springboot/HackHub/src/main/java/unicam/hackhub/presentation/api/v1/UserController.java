package unicam.hackhub.presentation.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.invitation.InvitationHandler;
import unicam.hackhub.application.team.CreateTeamHandler;
import unicam.hackhub.presentation.dto.request.TeamRequest;

@Validated
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final CreateTeamHandler createTeamHandler;
    private final InvitationHandler invitationHandler;

    public UserController(CreateTeamHandler createTeamHandler, InvitationHandler invitationHandler) {
        this.createTeamHandler = createTeamHandler;
        this.invitationHandler = invitationHandler;
    }

    @RequestMapping(value = "/team", method = RequestMethod.POST)
    public ResponseEntity<Object> createTeam(@RequestBody TeamRequest teamRequest) {
         return ResponseEntity
                 .status(HttpStatus.CREATED)
                 .body(createTeamHandler.createTeam(teamRequest.user(), teamRequest.teamName(), teamRequest.emails()));
    }

    @RequestMapping(value = "/{userId}/invitation", method = RequestMethod.GET)
    public ResponseEntity<Object> getInvitations(@PathVariable String userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(invitationHandler.getInvitations(userId));
    }

    @RequestMapping(value = "/{userId}/invitation/{teamName}", method = RequestMethod.GET)
    public ResponseEntity<Object> acceptInvitation(@PathVariable String userId, @PathVariable String teamName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(invitationHandler.acceptInvitation(userId, teamName));
    }

    @RequestMapping(value = "/{userId}/invitation/{teamName}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> declineInvitation(@PathVariable String userId, @PathVariable String teamName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(invitationHandler.declineInvitation(userId, teamName));
    }
}
