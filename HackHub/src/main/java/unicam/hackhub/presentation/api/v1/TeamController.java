package unicam.hackhub.presentation.api.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.hackathon.SubmissionHandler;
import unicam.hackhub.application.invitation.InvitationHandler;
import unicam.hackhub.application.supportRequest.CalendarService;
import unicam.hackhub.application.team.RegisterTeamHandler;
import unicam.hackhub.domain.hackathon.model.Submission;
import unicam.hackhub.presentation.dto.request.MentorAvailabilityRequest;
import unicam.hackhub.presentation.dto.request.SubmissionRequest;
import unicam.hackhub.presentation.dto.request.SupportRequest;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/team")
public class TeamController {

    private final InvitationHandler invitationHandler;
    private final SubmissionHandler submissionHandler;
    private final RegisterTeamHandler registerTeamHandler;
    private final CalendarService calendarService;

    @PreAuthorize("hasRole('TEAM_MEMBER')")
    @RequestMapping(value = "/invite", method = RequestMethod.POST)
    public ResponseEntity<Object> inviteUser(
            @RequestParam
            @NotBlank(message = "UserEmail è obbligatorio")
            String userEmail
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(invitationHandler.inviteUser(userEmail, getEmail()));
    }

    @PreAuthorize("hasRole('TEAM_MEMBER')")
    @RequestMapping(value = "/register/{hackathonId}", method = RequestMethod.POST)
    public ResponseEntity<Object> registerTeam(
            @PathVariable
            @NotNull(message = "Hackathon ID è obbligatorio")
            @Positive(message = "Hackathon ID deve essere positivo")
            Long hackathonId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(registerTeamHandler.registerTeam(getEmail(), hackathonId));
    }

    @PreAuthorize("hasRole('TEAM_MEMBER')")
    @RequestMapping(value = "/hackathon/{hackathonId}", method = RequestMethod.POST)
    public ResponseEntity<Object> addSubmission(
            @PathVariable
            @NotNull(message = "Hackathon ID è obbligatorio")
            @Positive(message = "Hackathon ID deve essere positivo")
            Long hackathonId,
            @Validated @RequestBody SubmissionRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(submissionHandler.addSubmission(getEmail(), hackathonId, new Submission(request.url())));
    }

    @PreAuthorize("hasRole('TEAM_MEMBER')")
    @RequestMapping(value = "/hackathon/{hackathonId}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateSubmission(
            @PathVariable
            @NotNull(message = "Hackathon ID è obbligatorio")
            @Positive(message = "Hackathon ID deve essere positivo")
            Long hackathonId,
            @Validated @RequestBody SubmissionRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(submissionHandler.updateSubmission(getEmail(), hackathonId, new Submission(request.url())));
    }

    @PreAuthorize("hasRole('TEAM_MEMBER')")
    @RequestMapping(value = "/mentor_availability", method = RequestMethod.GET)
    public ResponseEntity<Object> getMentorAvailability(@RequestBody MentorAvailabilityRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(calendarService.getFreeSlots(request.mentorEmail(), request.date()));
    }

    @PreAuthorize("hasRole('TEAM_MEMBER')")
    @RequestMapping(value = "/support", method = RequestMethod.POST)
    public ResponseEntity<Object> requestSupport(@RequestBody SupportRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(calendarService.requestSupport(
                        getEmail(),
                        request.hackathonId(),
                        request.mentorEmail(),
                        request.slot(),
                        request.date())
                );
    }

    private String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw new BadCredentialsException("Invalid user");
        }

        return authentication.getName();
    }
}
