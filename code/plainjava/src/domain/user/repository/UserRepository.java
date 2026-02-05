package plainjava.src.domain.user.repository;

import plainjava.src.domain.user.model.User;
import plainjava.src.domain.utils.repository.Find;
import plainjava.src.domain.utils.repository.Save;

public interface UserRepository extends
        Save<User>, Find<User, String> {
}
