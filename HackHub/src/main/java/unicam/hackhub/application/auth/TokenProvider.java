package unicam.hackhub.application.auth;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface TokenProvider {
    String generateToken(Map<String, Object> claims, UserDetails userDetails);
    long getExpirationTime();
}
