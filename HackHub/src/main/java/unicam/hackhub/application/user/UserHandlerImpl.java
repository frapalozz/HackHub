package unicam.hackhub.application.user;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;

@Service
@Primary
@AllArgsConstructor
public class UserHandlerImpl implements UserHandler{

    private final UserRepository userRepository;

    @Override
    public void deleteUser(String userEmail) {
        User user = getUser(userEmail);

        userRepository.delete(user);
    }

    @Override
    public void editProfile(String userEmail, String name) {
        User user = getUser(userEmail);

        if(name != null && !name.isEmpty()) {
            user.setName(name);
        }

        userRepository.save(user);
    }

    private User getUser(String userEmail) {
        User user = userRepository.findById(userEmail).orElse(null);

        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return user;
    }
}
