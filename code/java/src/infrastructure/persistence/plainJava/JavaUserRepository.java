package code.java.src.infrastructure.persistence.plainJava;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import code.java.src.domain.user.model.User;
import code.java.src.domain.user.repository.UserRepository;

public class JavaUserRepository implements UserRepository {

    private Set<User> users = new HashSet<>();

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

    @Override
    public void save(User user) {
        User userPresent = this.findById(user.getEmail());

        if(userPresent == null) {
            this.users.add(user);
        } else {
            this.users.remove(userPresent);
            this.users.add(user);
        }
    }
    
}
