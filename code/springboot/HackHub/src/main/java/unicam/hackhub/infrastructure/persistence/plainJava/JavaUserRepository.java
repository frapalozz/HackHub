package unicam.hackhub.infrastructure.persistence.plainJava;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;

public class JavaUserRepository implements UserRepository {

    private final Set<User> users = new HashSet<>();

    @Override
    public User findById(String email) {
        return this.users.stream()
            .filter(u -> u.getEmail() == email)
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<User> findAll(List<String> emails) {
        return this.users.stream()
        .filter(u -> emails.contains(u.getEmail()))
        .toList();
    }

    @Override
    public User save(User user) {
        if(user == null) return null;
        User userPresent = this.findById(user.getEmail());

        if(userPresent == null) {
            this.users.add(user);
        } else {
            this.users.remove(userPresent);
            this.users.add(user);
        }

        return user;
    }

    @Override
    public void saveAll(List<User> entities) {
        entities.forEach(this::save);
    }

}
