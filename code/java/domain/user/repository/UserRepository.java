package code.java.domain.user.repository;

import java.util.List;

import code.java.domain.user.model.User;

public interface UserRepository {
    
    User findById(String email);
    List<User> findAllById(List<String> emails);
}
