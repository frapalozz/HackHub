package unicam.hackhub.presentation.api.v1;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.invitation.InvitationHandler;
import unicam.hackhub.application.team.CreateTeamHandler;
import unicam.hackhub.application.user.UserHandler;
import unicam.hackhub.presentation.dto.request.TeamRequest;
import unicam.hackhub.presentation.dto.request.UserRequest;

import java.security.Principal;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final CreateTeamHandler createTeamHandler;
    private final InvitationHandler invitationHandler;
    private final UserHandler userHandler;

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/team", method = RequestMethod.POST)
    public ResponseEntity<Object> createTeam(@Validated @RequestBody TeamRequest teamRequest) {
         return ResponseEntity
                 .status(HttpStatus.CREATED)
                 .body(createTeamHandler.createTeam(teamRequest.user(), teamRequest.teamName(), teamRequest.emails()));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/{userId}/invitation", method = RequestMethod.GET)
    public ResponseEntity<Object> getInvitations(
            @PathVariable
            @NotBlank(message = "Userid è obbligatorio")
            String userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(invitationHandler.getInvitations(userId));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/{userId}/invitation/{teamName}", method = RequestMethod.GET)
    public ResponseEntity<Object> acceptInvitation(
            @PathVariable
            @NotBlank(message = "Userid è obbligatorio")
            String userId,
            @PathVariable
            @NotBlank(message = "Team name è obbligatorio")
            String teamName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(invitationHandler.acceptInvitation(userId, teamName));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/{userId}/invitation/{teamName}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> declineInvitation(
            @PathVariable
            @NotBlank(message = "Userid è obbligatorio")
            String userId,
            @PathVariable
            @NotBlank(message = "Team name è obbligatorio")
            String teamName
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(invitationHandler.declineInvitation(userId, teamName));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/removeprofile", method = RequestMethod.DELETE)
    public ResponseEntity<Object> removeProfile(
            @RequestParam
            @NotBlank(message = "userEmail è obbligatorio") String userEmail
    ) {
        userHandler.deleteUser(userEmail);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Account deleted");
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/updateprofile", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateProfile(@Validated @RequestBody UserRequest userRequest) {
        userHandler.editProfile(userRequest.userId(), userRequest.name(), userRequest.email());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User updated");
    }
}
