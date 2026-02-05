package code.java.src.application.invitation;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import code.java.src.domain.invitation.domain.Invitation;
import code.java.src.domain.invitation.repository.InvitationRepository;
import code.java.src.domain.team.model.Team;
import code.java.src.domain.user.model.User;
import code.java.src.domain.user.repository.UserRepository;

public class InvitationService {
    
    private UserRepository userRepository;
    private InvitationRepository invitationRepository;

    public void createInvitations(Team team, List<String> invitedUsers) {
        List<User> users = userRepository.findAllById(invitedUsers);

        List<Invitation> invitations = users.stream()
            .map(user -> new Invitation(LocalDate.now(), team, user))
            .collect(Collectors.toList());

        invitationRepository.saveAll(invitations);
    }
}
