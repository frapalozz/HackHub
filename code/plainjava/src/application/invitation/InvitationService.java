package plainjava.src.application.invitation;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import plainjava.src.domain.invitation.domain.Invitation;
import plainjava.src.domain.invitation.repository.InvitationRepository;
import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.user.model.User;
import plainjava.src.domain.user.repository.UserRepository;

public class InvitationService {
    
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;

    public InvitationService(UserRepository ur, InvitationRepository ir) {
        this.userRepository = ur;
        this.invitationRepository = ir;
    }

    /**
     * Create bulk invitations
     * @param team team associated to the invitation
     * @param invitedUsers invited email list
     */
    public void createInvitations(Team team, List<String> invitedUsers) {
        List<User> users = userRepository.findAll(invitedUsers);

        List<Invitation> invitations = users.stream()
            .map(user -> new Invitation(LocalDate.now(), team, user))
            .collect(Collectors.toList());

        invitationRepository.saveAll(invitations);
    }
}
