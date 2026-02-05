package code.java.src.infrastructure.persistence.plainJava;

import java.util.List;
import java.util.Set;

import code.java.src.domain.user.model.User;
import code.java.src.domain.user.repository.UserRepository;

public class JavaUserRepository implements UserRepository {

    private Set<User> users;

    @Override
    public User findById(String email) {
        return this.users.stream()
            .filter(u -> u.getEmail() == email)
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<User> findAllById(List<String> emails) {
        return this.users.stream()
        .filter(u -> emails.contains(u.getEmail()))
        .toList();
    }
    
}
