package unicam.hackhub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.staff.repository.StaffRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private StaffRepository staffRepository;

    private final static Staff[] STAFF = {
            new Staff("john", "john.doe@tech.com"),
            new Staff("jane", "jane.smith@innovate.com"),
            new Staff("alex", "alex.wong@ai-labs.com")
    };

    private final static User[] users = {
            new User("john", "john.doe@tech.com"),
            new User("jane", "jane.smith@innovate.com"),
    };

    @Override
    public void run(String... args) throws Exception {
        staffRepository.saveAll(Arrays.asList(STAFF));
        userRepository.saveAll(Arrays.asList(users));
    }
}
