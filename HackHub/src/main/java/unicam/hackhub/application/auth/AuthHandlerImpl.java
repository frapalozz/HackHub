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


            Map<String, Object> claims = new HashMap<>();
            claims.put("name", user.getName());
            claims.put("email", user.getEmail());
            claims.put("role", user.getRole().name());


            String jwtToken = jwtTokenUtil.generateToken(claims, userDetails);

            return new TokenResponse(jwtToken, "Bearer", String.valueOf(jwtTokenUtil.getExpirationTime()));
        } else {
            Staff staff = staffRepository.findById(email)
                    .orElseThrow(() -> new BadCredentialsException(email));

            matchPassword(password, staff.getPassword());

            org.springframework.security.core.userdetails.User userDetails =
                    new org.springframework.security.core.userdetails.User(
                            staff.getEmail(),
                            staff.getPassword(),
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + staff.getRole().name())
                            ));


            Map<String, Object> claims = new HashMap<>();
            claims.put("name", staff.getName());
            claims.put("email", staff.getEmail());
            claims.put("role", staff.getRole().name());

            String jwtToken = jwtTokenUtil.generateToken(claims, userDetails);

            return new TokenResponse(jwtToken, "Bearer", String.valueOf(jwtTokenUtil.getExpirationTime()));
        }
    }

    @Override
    public TokenResponse register(String name, String email, String password, String type) {

        if(type.equalsIgnoreCase("USER")) {
            User user = userRepository.findById(email).orElse(null);

            if(user != null) {
                throw new BadCredentialsException("Email already used");
            }

            String passwordEncoded = passwordEncoder.encode(password);

            User newUser = new User(name, email);
            newUser.setPassword(passwordEncoded);
            newUser.setRole(Role.USER);

            userRepository.save(newUser);

            org.springframework.security.core.userdetails.User userDetails =
                    new org.springframework.security.core.userdetails.User(
                            newUser.getEmail(),
                            newUser.getPassword(),
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + newUser.getRole().name())
                            ));

            Map<String, Object> claims = new HashMap<>();
            claims.put("name", newUser.getName());
            claims.put("email", newUser.getEmail());
            claims.put("role", newUser.getRole().name());

            String jwtToken = jwtTokenUtil.generateToken(claims, userDetails);

            return new TokenResponse(jwtToken, "Bearer", String.valueOf(jwtTokenUtil.getExpirationTime()));
        } else {
            Staff staff = staffRepository.findById(email).orElse(null);

            if(staff != null) {
                throw new BadCredentialsException("Email already used");
            }

            String passwordEncoded = passwordEncoder.encode(password);

            Staff newStaff = new Staff(name, email);
            newStaff.setPassword(passwordEncoded);
            newStaff.setRole(Role.USER);

            staffRepository.save(newStaff);

            org.springframework.security.core.userdetails.User userDetails =
                    new org.springframework.security.core.userdetails.User(
                            newStaff.getEmail(),
                            newStaff.getPassword(),
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + newStaff.getRole().name())
                            ));

            Map<String, Object> claims = new HashMap<>();
            claims.put("name", newStaff.getName());
            claims.put("email", newStaff.getEmail());
            claims.put("role", newStaff.getRole().name());

            String jwtToken = jwtTokenUtil.generateToken(claims, userDetails);

            return new TokenResponse(jwtToken, "Bearer", String.valueOf(jwtTokenUtil.getExpirationTime()));
        }
    }

    private void matchPassword(String passworda, String passwordb) {
        if(!passwordEncoder.matches(passworda, passwordb)) {
            throw new BadCredentialsException("Wrong password");
        }
    }
}
