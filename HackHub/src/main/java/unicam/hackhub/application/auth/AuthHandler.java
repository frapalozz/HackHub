package unicam.hackhub.application.auth;

import unicam.hackhub.application.dto.response.TokenResponse;

public interface AuthHandler {

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param email    the user's email address
     * @param password the user's password
     * @param type     the user type ("USER" or "STAFF") – determines role or access level
     * @return a {@link TokenResponse} containing the generated JWT token
     */
    TokenResponse login(String email, String password, String type);

    /**
     * Registers a new user and returns a JWT token.
     *
     * @param name     the user's full name
     * @param email    the user's email address (must be unique)
     * @param password the user's chosen password
     * @param type     the user type ("USER" or "STAFF") – defines role and permissions
     * @return a {@link TokenResponse} containing the generated JWT token for the newly registered user
     */
    TokenResponse register(String name, String email, String password, String type);
}
