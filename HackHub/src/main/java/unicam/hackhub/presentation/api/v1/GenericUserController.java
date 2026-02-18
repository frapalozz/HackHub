package unicam.hackhub.presentation.api.v1;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import unicam.hackhub.application.hackathon.HackathonViewHandler;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/visitor")
public class GenericUserController {

    private final HackathonViewHandler hackathonViewHandler;

    @RequestMapping(value = "/hackathons", method = RequestMethod.GET)
    public ResponseEntity<Object> getHackathons() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hackathonViewHandler.getPublicHackathons());
    }

    @RequestMapping(value = "/hackathons/{hackathonId}", method = RequestMethod.GET)
    public ResponseEntity<Object> getHackathon(
            @NotNull
            @Positive
            @PathVariable Long hackathonId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hackathonViewHandler.getHackathonDetails(hackathonId));
    }
}
