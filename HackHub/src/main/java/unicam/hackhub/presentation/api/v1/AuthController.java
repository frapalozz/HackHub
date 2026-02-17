package unicam.hackhub.presentation.api.v1;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.auth.AuthHandler;
import unicam.hackhub.presentation.dto.request.LogInRequest;
import unicam.hackhub.presentation.dto.request.SignInRequest;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthHandler authHandler;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Object> login(@Valid @RequestBody LogInRequest request) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authHandler.login(request.email(), request.password(), request.type()));
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }

    }

    @RequestMapping(value = "/signin" , method = RequestMethod.POST)
    public ResponseEntity<Object> register(@Valid @RequestBody SignInRequest request) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authHandler.register(request.name(), request.email(), request.password(), request.type()));
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }
}
