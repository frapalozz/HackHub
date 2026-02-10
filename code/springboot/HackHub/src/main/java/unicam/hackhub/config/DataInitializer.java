package unicam.hackhub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import unicam.hackhub.domain.staffMember.model.StaffMember;
import unicam.hackhub.domain.staffMember.repository.StaffMemberRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private StaffMemberRepository staffMemberRepository;

    private final static StaffMember[] staffMembers = {
            new StaffMember("john", "john.doe@tech.com"),
            new StaffMember("jane", "jane.smith@innovate.com"),
            new StaffMember("alex", "alex.wong@ai-labs.com")
    };

    private final static User[] users = {
            new User("john", "john.doe@tech.com"),
            new User("jane", "jane.smith@innovate.com"),
    };

    @Override
    public void run(String... args) throws Exception {
        staffMemberRepository.saveAll(Arrays.asList(staffMembers));
        userRepository.saveAll(Arrays.asList(users));
    }
}
