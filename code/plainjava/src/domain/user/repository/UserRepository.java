package plainjava.src.domain.user.repository;

import java.util.List;

import plainjava.src.domain.user.model.User;

public interface UserRepository {
    
    void save(User user);
    User findById(String email);
    List<User> findAllById(List<String> emails);
}
