package unicam.hackhub.presentation.api.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.dto.command.SubmissionCommand;
import unicam.hackhub.application.hackathon.HackathonHandler;
import unicam.hackhub.application.submission.SubmissionHandler;
import unicam.hackhub.application.invitation.InvitationHandler;
import unicam.hackhub.application.supportRequest.CalendarHandler;
import unicam.hackhub.application.team.TeamHandler;
import unicam.hackhub.presentation.dto.request.MentorAvailabilityRequest;
import unicam.hackhub.presentation.dto.request.SubmissionRequest;
import unicam.hackhub.presentation.dto.request.SupportRequest;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/team")
@PreAuthorize("hasRole('TEAM_MEMBER')")
public class TeamController {

    private final InvitationHandler invitationHandler;
    private final SubmissionHandler submissionHandler;
    private final TeamHandler teamHandler;
    private final HackathonHandler hackathonHandler;
    private final CalendarHandler calendarHandler;

    @RequestMapping(value = "/invite", method = RequestMethod.POST)
    public ResponseEntity<Object> inviteUser(
            @RequestParam
            @NotBlank(message = "UserEmail è obbligatorio")
            String userEmail
    ) {
        String authEmail = getEmail();
        if(userEmail.equals(authEmail)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Cannot invite self");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(invitationHandler.inviteUser(userEmail, authEmail));
    }

    @RequestMapping(value = "/register/{hackathonId}", method = RequestMethod.POST)
    public ResponseEntity<Object> registerTeam(
            @PathVariable
            @NotNull(message = "Hackathon ID è obbligatorio")
            @Positive(message = "Hackathon ID deve essere positivo")
            Long hackathonId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(teamHandler.registerTeam(getEmail(), hackathonId));
    }

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
                .body(submissionHandler.addSubmission(new SubmissionCommand(getEmail(), hackathonId, request.url())));
    }

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
                .body(submissionHandler.updateSubmission(new SubmissionCommand(getEmail(), hackathonId, request.url())));
    }

    @RequestMapping(value = "/mentor_availability", method = RequestMethod.GET)
    public ResponseEntity<Object> getMentorAvailability(@RequestBody MentorAvailabilityRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(calendarHandler.getFreeSlots(request.mentorEmail(), request.date()));
    }

    @RequestMapping(value = "/support", method = RequestMethod.POST)
    public ResponseEntity<Object> requestSupport(@RequestBody SupportRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(calendarHandler.requestSupport(
                        getEmail(),
                        request.hackathonId(),
                        request.mentorEmail(),
                        request.slot(),
                        request.date())
                );
    }

    @RequestMapping(value = "/hackathon/{hackathonId}", method = RequestMethod.GET)
    public ResponseEntity<Object> getSubmission(
            @NonNull
            @Positive
            @PathVariable Long hackathonId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(submissionHandler.getSubmissionTeam(getEmail(), hackathonId));
    }

    @RequestMapping(value = "/hackathons", method = RequestMethod.GET)
    public ResponseEntity<Object> getHackathons() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hackathonHandler.getParticipatingHackathons(getEmail()));
    }

    private String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw new BadCredentialsException("Invalid user");
        }

        return authentication.getName();
    }
}
