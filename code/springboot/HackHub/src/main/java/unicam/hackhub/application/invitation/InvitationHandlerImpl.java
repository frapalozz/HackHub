package unicam.hackhub.application.invitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.invitation.domain.Invitation;
import unicam.hackhub.domain.invitation.domain.InvitationId;
import unicam.hackhub.domain.invitation.repository.InvitationRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.team.repository.TeamRepository;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Primary
public class InvitationHandlerImpl implements InvitationHandler {

    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;

    @Autowired
    public InvitationHandlerImpl(UserRepository userRepository, InvitationRepository invitationRepository,
                                 TeamRepository teamRepository, HackathonRepository HackathonRepository) {
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
        this.teamRepository = teamRepository;
        this.hackathonRepository = HackathonRepository;
    }

    @Override
    public String inviteUser(String userId, String teamName) {

        if(!validateEmail(userId)) {
            throw new IllegalArgumentException("Invalid email address");
        }

        User user = userRepository.findById(userId).orElse(null);

        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Team team = teamRepository.findById(teamName).orElse(null);

        if(invitationRepository.existsById(new InvitationId(team, user))) {
            throw new IllegalArgumentException("Invitation already exists");
        }

        Invitation invitation = new Invitation(LocalDate.now(), team, user);

        invitationRepository.save(invitation);

        return "Invitation saved";
    }

    @Override
    public String acceptInvitation(InvitationId invitationId) {

        Invitation invitation = findInvitation(invitationId);

        if(hackathonRepository.inActiveHackathon(invitation.getId().getTeam())) {
            throw new IllegalArgumentException("Can't accept, team in a active hackathon");
        }

        invitation.accept();

        invitationRepository.delete(invitation);

        return "Invitation accepted";
    }

    @Override
    public String declineInvitation(InvitationId invitationId) {

        Invitation invitation = findInvitation(invitationId);

        invitationRepository.delete(invitation);

        return "Invitation deleted";
    }

    @Override
    public List<Invitation> getInvitations(String userId) {
        return invitationRepository.findAll(userId);
    }

    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        if(email == null || email.isEmpty()) return false;

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private Invitation findInvitation(InvitationId invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId).orElse(null);

        if(invitation == null) {
            throw new IllegalArgumentException("Invitation not found");
        }

        return invitation;
    }
}
