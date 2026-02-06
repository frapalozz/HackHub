package plainjava.src.application.invitation;

import plainjava.src.domain.hackathon.repository.HackathonRepository;
import plainjava.src.domain.invitation.domain.Invitation;
import plainjava.src.domain.invitation.domain.InvitationId;
import plainjava.src.domain.invitation.repository.InvitationRepository;
import plainjava.src.domain.team.model.Team;
import plainjava.src.domain.team.repository.TeamRepository;
import plainjava.src.domain.user.model.User;
import plainjava.src.domain.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvitationHandlerImpl implements InvitationHandler {

    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;

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

        User user = userRepository.findById(userId);

        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Team team = teamRepository.findById(teamName);

        if(invitationRepository.existsById(new InvitationId(team, user))) {
            throw new IllegalArgumentException("Invitation already exists");
        }

        Invitation invitation = new Invitation(LocalDate.now(), team, user);

        invitationRepository.save(invitation);

        return "Invitation saved";
    }

    @Override
    public String acceptInvitation(InvitationId invitationId) {
        return "";
    }

    @Override
    public String declineInvitation(InvitationId invitationId) {
        return "";
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
}
