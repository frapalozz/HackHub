package unicam.hackhub.presentation.api.v1;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import unicam.hackhub.application.auth.AuthHandler;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthHandler authHandler;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<Object> login(
            @NotBlank @RequestParam String email,
            @NotBlank @RequestParam String password,
            @NotBlank @RequestParam String type
    ) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authHandler.login(email, password, type));
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED).build();
        }

        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put("email", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        return ResponseEntity.ok(response);
         */

    }
}
