package unicam.hackhub.application.auth;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import unicam.hackhub.application.dto.response.TokenResponse;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.staff.repository.StaffRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;
import unicam.hackhub.infrastructure.security.JwtTokenUtil;

import java.util.Collections;

@Service
@Primary
@AllArgsConstructor
public class AuthHandlerImpl implements AuthHandler {

    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;

    @Override
    public TokenResponse login(String email, String password, String type) {
        if(type.equalsIgnoreCase("USER")) {
            User user = userRepository.findById(email)
                    .orElseThrow(() -> new BadCredentialsException(email));

            matchPassword(password, user.getPassword());

            org.springframework.security.core.userdetails.User userDetails =
                    new org.springframework.security.core.userdetails.User(
                            user.getEmail(),
                            user.getPassword(),
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                    ));

            /*
            Map<String, Object> claims = new HashMap<>();
            claims.put("name", user.getName());
            claims.put("email", user.getEmail());
            claims.put("role", user.getRole().name());
             */

            String jwtToken = jwtTokenUtil.generateToken(userDetails);

            TokenResponse tokenResponse = new TokenResponse(jwtToken, "Bearer");
        } else {
            Staff staff = staffRepository.findById(email)
                    .orElseThrow(() -> new BadCredentialsException(email));

            matchPassword(password, staff.getPassword());

            org.springframework.security.core.userdetails.User userDetails =
                    new org.springframework.security.core.userdetails.User(
                            staff.getEmail(),
                            staff.getPassword(),
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                            ));

            /*
            Map<String, Object> claims = new HashMap<>();
            claims.put("name", user.getName());
            claims.put("email", user.getEmail());
            claims.put("role", user.getRole().name());
             */

            String jwtToken = jwtTokenUtil.generateToken(userDetails);

            TokenResponse tokenResponse = new TokenResponse(jwtToken, "Bearer");
        }
        throw new BadCredentialsException("Wrong password");
    }

    private void matchPassword(String passworda, String passwordb) {
        if(!passwordEncoder.matches(passworda, passwordb)) {
            throw new BadCredentialsException("Wrong password");
        }
    }
}
