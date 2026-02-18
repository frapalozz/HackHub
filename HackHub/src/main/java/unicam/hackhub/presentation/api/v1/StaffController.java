package unicam.hackhub.presentation.api.v1;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
import unicam.hackhub.application.hackathon.CreateHackathonHandler;
import unicam.hackhub.application.hackathon.HackathonHandler;
import unicam.hackhub.application.hackathon.HackathonViewHandler;
import unicam.hackhub.application.hackathon.SubmissionHandler;
import unicam.hackhub.application.report.ReportHandler;
import unicam.hackhub.application.supportRequest.CalendarService;
import unicam.hackhub.presentation.dto.mapper.HackathonMapper;
import unicam.hackhub.presentation.dto.request.*;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/staff")
@PreAuthorize("hasRole('STAFF')")
public class StaffController {

    private final CreateHackathonHandler createHackathonHandler;
    private final SubmissionHandler submissionHandler;
    private final HackathonHandler hackathonHandler;
    private final ReportHandler reportHandler;
    private final HackathonViewHandler hackathonViewHandler;
    private final CalendarService calendarService;
    private final HackathonMapper hackathonMapper;

    @RequestMapping(value = "/hackathon", method = RequestMethod.POST)
    public ResponseEntity<Object> createHackathon(@Valid @RequestBody HackathonRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createHackathonHandler
                        .createHackathon(hackathonMapper.toCreateHackathonRequest(request, getEmail())));
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
                .body(hackathonHandler.declareWinner(getEmail(), hackathonId, teamName));
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
            @Valid @RequestBody ValuateSubmissionRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(submissionHandler
                        .valuateSubmission(getEmail(), hackathonId, teamName, request.vote(), request.message()));
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
            @Valid @RequestBody ValuateSubmissionRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(submissionHandler
                        .editValuation(getEmail(), hackathonId, teamName, request.vote(), request.message()));
    }

    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public ResponseEntity<Object> reportTeam(@Validated @RequestBody ReportRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reportHandler.report(getEmail(), request.teamName(), request.hackathonId(), request.description()));
    }

    @RequestMapping(value = "/hackathon/{hackathonId}/addMentors", method = RequestMethod.PUT)
    public ResponseEntity<Object> addMentors(@PathVariable Long hackathonId, @Validated @RequestBody AddMentorsRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hackathonHandler.addMentors(getEmail(), hackathonId, request.emailList()));
    }

    @RequestMapping(value = "/support_request/{requestId}/accept", method = RequestMethod.PUT)
    public ResponseEntity<Object> acceptSupportRequest(@PathVariable Long requestId,
                                                       @RequestBody AcceptSupportRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(calendarService.acceptRequest(getEmail(), requestId, request.linkCall()));
    }

    @RequestMapping(value = "/support_request/{requestId}/decline", method = RequestMethod.PUT)
    public ResponseEntity<Object> declineSupportRequest(@PathVariable Long requestId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(calendarService.declineRequest(getEmail(), requestId));
    }

    @RequestMapping(value = "/hackathons", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllHackathons() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hackathonViewHandler.getAllHackathons());
    }

    @RequestMapping(value = "/hackathons/me", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllHackathonsMe() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hackathonViewHandler.getAssignedHackathons(getEmail()));
    }

    @RequestMapping(value = "/hackathon/{hackathonId}/submissions", method = RequestMethod.GET)
    public ResponseEntity<Object> getAssignedHackathonSubmissions(
            @NonNull
            @Positive
            @PathVariable Long hackathonId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hackathonViewHandler.getSubmissions(getEmail(), hackathonId));
    }

    @RequestMapping(value = "/hackathon/{hackathonId}/submissions/{submissionId}", method = RequestMethod.GET)
    public ResponseEntity<Object> getSubmission(
            @NonNull
            @Positive
            @PathVariable Long hackathonId,
            @NonNull
            @Positive
            @PathVariable Long submissionId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hackathonViewHandler.getSubmissionStaff(getEmail(), submissionId));
    }

    @RequestMapping(value = "/supportRequests", method = RequestMethod.GET)
    public ResponseEntity<Object> getSupportRequests() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hackathonViewHandler.getSupportRequests(getEmail()));
    }

    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public ResponseEntity<Object> getReports() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hackathonViewHandler.getReports(getEmail()));
    }

    private String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw new BadCredentialsException("Invalid user");
        }

        return authentication.getName();
    }
}
