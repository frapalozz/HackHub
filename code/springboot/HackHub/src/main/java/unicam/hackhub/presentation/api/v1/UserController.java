package unicam.hackhub.presentation.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import unicam.hackhub.application.team.CreateTeamHandler;
import unicam.hackhub.presentation.dto.request.TeamRequest;

@Validated
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final CreateTeamHandler createTeamHandler;

    public UserController(CreateTeamHandler createTeamHandler) {
        this.createTeamHandler = createTeamHandler;
    }

    @RequestMapping(value = "/team", method = RequestMethod.POST)
    public ResponseEntity<Object> createTeam(@RequestBody TeamRequest teamRequest) {
         return ResponseEntity
                 .status(HttpStatus.CREATED)
                 .body(createTeamHandler.createTeam(teamRequest.user(), teamRequest.teamName(), teamRequest.emails()));
    }
}
