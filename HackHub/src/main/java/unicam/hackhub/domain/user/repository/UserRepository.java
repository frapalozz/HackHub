package unicam.hackhub.domain.user.repository;

import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.utils.repository.Delete;
import unicam.hackhub.domain.utils.repository.Find;
import unicam.hackhub.domain.utils.repository.Save;

public interface UserRepository extends
        Save<User>, Find<User, String>, Delete<User> {
}
