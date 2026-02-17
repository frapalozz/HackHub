package unicam.hackhub.application.invitation;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unicam.hackhub.domain.invitation.domain.Invitation;
import unicam.hackhub.domain.invitation.repository.InvitationRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InvitationService {
    
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;

    /**
     * Create bulk invitations
     * @param team team associated to the invitation
     * @param invitedUsers invited email list
     */
    public void createInvitations(Team team, List<String> invitedUsers) {
        if(invitedUsers == null || invitedUsers.isEmpty()) return;

        List<User> users = userRepository.findAllById(invitedUsers);

        List<Invitation> invitations = users.stream()
            .map(user -> new Invitation(LocalDate.now(), team, user))
            .collect(Collectors.toList());

        invitationRepository.saveAll(invitations);
    }
}
