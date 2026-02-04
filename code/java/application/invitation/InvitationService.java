package code.java.application.invitation;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import code.java.domain.invitation.domain.Invitation;
import code.java.domain.invitation.repository.InvitationRepository;
import code.java.domain.team.model.Team;
import code.java.domain.user.model.User;
import code.java.domain.user.repository.UserRepository;

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
