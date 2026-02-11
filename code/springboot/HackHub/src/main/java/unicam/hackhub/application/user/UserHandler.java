package unicam.hackhub.application.user;

public interface UserHandler {

    void deleteUser(String userEmail);
    void editProfile(String userEmail, String name, String email);
}
