package unicam.hackhub.application.user;

public interface UserHandler {

    /**
     * Deletes a user account permanently.
     *
     * @param userEmail the email of the user to delete
     */
    void deleteUser(String userEmail);

    /**
     * Updates a user's profile information.
     *
     * @param userEmail the email of the user to edit
     * @param name      the new name for the user
     */
    void editProfile(String userEmail, String name);
}
