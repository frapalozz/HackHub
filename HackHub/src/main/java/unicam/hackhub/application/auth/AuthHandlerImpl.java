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
import unicam.hackhub.domain.utils.Role;
import unicam.hackhub.infrastructure.security.JwtTokenUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        if (type.equalsIgnoreCase("user")) {
            User user = userRepository.findById(email)
                    .orElseThrow(() -> new BadCredentialsException(email));

            return getTokenResponse(password, user.getPassword(), user.getEmail(), user.getRole(), user.getName());
        }

        Staff staff = staffRepository.findById(email)
                .orElseThrow(() -> new BadCredentialsException(email));

        return getTokenResponse(password, staff.getPassword(), staff.getEmail(), staff.getRole(), staff.getName());
    }

    @Override
    public TokenResponse register(String name, String email, String password, String type) {

        if(type.equalsIgnoreCase("USER")) {
            User user = userRepository.findById(email).orElse(null);

            if(user != null) {
                throw new BadCredentialsException("Email already used");
            }

            User newUser = new User(name, email);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setRole(Role.USER);

            userRepository.save(newUser);

            return buildTokenResponse(newUser.getPassword(), newUser.getEmail(), newUser.getName(), newUser.getRole());
        }
        
        Staff staff = staffRepository.findById(email).orElse(null);

        if(staff != null) {
            throw new BadCredentialsException("Email already used");
        }

        Staff newStaff = new Staff(name, email);
        newStaff.setPassword(passwordEncoder.encode(password));
        newStaff.setRole(Role.STAFF);

        staffRepository.save(newStaff);

        return buildTokenResponse(newStaff.getPassword(), newStaff.getEmail(), newStaff.getName(), newStaff.getRole());
    }

    private void matchPassword(String password, String password2) {
        if(!passwordEncoder.matches(password, password2)) {
            throw new BadCredentialsException("Wrong password");
        }
    }

    private TokenResponse getTokenResponse(String password, String passwordEncoded, String email, Role role, String name) {
        matchPassword(password, passwordEncoded);
        return buildTokenResponse(passwordEncoded, email, name, role);
    }

    private TokenResponse buildTokenResponse(String password, String email, String name, Role role) {
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        email,
                        password,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name())
                        ));

        Map<String, Object> claims = new HashMap<>();
        claims.put("name", name);
        claims.put("email", email);
        claims.put("role", role.name());

        String jwtToken = jwtTokenUtil.generateToken(claims, userDetails);

        return new TokenResponse(jwtToken, "Bearer", String.valueOf(jwtTokenUtil.getExpirationTime()));
    }
}
