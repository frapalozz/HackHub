package unicam.hackhub.presentation.api.v1;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.invitation.InvitationHandler;
import unicam.hackhub.application.team.CreateTeamHandler;
import unicam.hackhub.application.user.UserHandler;
import unicam.hackhub.presentation.dto.request.TeamRequest;
import unicam.hackhub.presentation.dto.request.UserRequest;

@Slf4j
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
                 .body(createTeamHandler.createTeam(getEmail(), teamRequest.teamName(), teamRequest.emails()));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/invitations", method = RequestMethod.GET)
    public ResponseEntity<Object> getInvitations() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(invitationHandler.getInvitations(getEmail()));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/invitation/{teamName}", method = RequestMethod.GET)
    public ResponseEntity<Object> acceptInvitation(
            @PathVariable
            @NotBlank(message = "Team name è obbligatorio")
            String teamName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(invitationHandler.acceptInvitation(getEmail(), teamName));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/invitation/{teamName}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> declineInvitation(
            @PathVariable
            @NotBlank(message = "Team name è obbligatorio")
            String teamName
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(invitationHandler.declineInvitation(getEmail(), teamName));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/deleteProfile", method = RequestMethod.DELETE)
    public ResponseEntity<Object> removeProfile() {
        userHandler.deleteUser(getEmail());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Account deleted");
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/updateProfile", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateProfile(@Validated @RequestBody UserRequest userRequest) {
        userHandler.editProfile(getEmail(), userRequest.name(), userRequest.email());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User updated");
    }

    private String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw new BadCredentialsException("Invalid user");
        }

        return authentication.getName();
    }
}
