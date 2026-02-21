package unicam.hackhub.application.hackathon;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import unicam.hackhub.application.dto.mapper.HackathonMapper;
import unicam.hackhub.application.dto.request.CreateHackathonRequest;
import unicam.hackhub.application.dto.response.AssignedHackathonResponse;
import unicam.hackhub.application.dto.response.HackathonResponse;
import unicam.hackhub.application.payment.PaymentService;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.staff.repository.StaffRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Primary
@AllArgsConstructor
public class HackathonHandlerImpl implements HackathonHandler {

    private final HackathonRepository hackathonRepository;
    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;

    private final HackathonMapper hackathonMapper;

    @Override
    public String createHackathon(CreateHackathonRequest request) {

        checkLogicDateOrder(request);
        checkSelfStaff(request);

        Staff organizer = staffRepository.findById(request.organizerEmail()).orElse(null);
        Staff judge = staffRepository.findById(request.judgeEmail()).orElse(null);
        List<Staff> mentors = request.mentorsEmails().stream()
                .map(e -> staffRepository.findById(e).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        if(organizer == null || judge == null || mentors.size() != request.mentorsEmails().size()) {
            throw new IllegalArgumentException("Staff not found");
        }

        Hackathon hackathon = new Hackathon(
                request.name(),
                request.location(),
                request.subscriptionDeadline(),
                request.hackathonPeriod(),
                request.maxTeamSize(),
                request.requirements(),
                request.prize(), organizer,
                judge,
                new HashSet<>(mentors));

        hackathonRepository.save(hackathon);

        return "Hackathon " + hackathon.getName() + " created";
    }

    @Override
    public String declareWinner(String organizerEmail, Long hackathonId, String teamName) {

        Hackathon hackathon = getHackathon(hackathonId);
        if(!hackathon.getOrganizer().getEmail().equals(organizerEmail)) {
            throw new AccessDeniedException("Access denied");
        }

        hackathon.declareWinner(teamName);

        paymentService.transferPrize(hackathon.getPrize(), teamName);

        hackathonRepository.save(hackathon);

        return "winner added";
    }

    @Override
    public String addMentors(String organizerEmail, Long hackathonId, List<String> mentorsList) {

        Hackathon hackathon = getHackathon(hackathonId);

        if(!hackathon.getOrganizer().getEmail().equals(organizerEmail)) {
            throw new AccessDeniedException("Access denied");
        }

        checkStaffAlreadyPresent(mentorsList, hackathon);

        List<Staff> mentors = staffRepository.findAllById(mentorsList);

        if(mentors.size() != mentorsList.size()) {
            throw new IllegalArgumentException("Mentors not found");
        }

        hackathon.addMentors(mentors);

        return "Mentors added";
    }

    @Override
    public List<HackathonResponse> getPublicHackathons() {
        return hackathonRepository.findPublicHackathons().stream().map(hackathonMapper::hackathonToHackathonResponse).collect(Collectors.toList());
    }

    @Override
    public HackathonResponse getHackathonDetails(Long hackathonId) {
        return hackathonMapper
                .hackathonToHackathonResponse(hackathonRepository
                        .findById(hackathonId)
                        .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"))
                );
    }

    @Override
    public List<HackathonResponse> getAllHackathons() {
        return hackathonRepository
                .findAll()
                .stream()
                .map(hackathonMapper::hackathonToHackathonResponse).
                toList();
    }

    @Override
    public List<AssignedHackathonResponse> getAssignedHackathons(String staffEmail) {

        return hackathonRepository
                .findAllWhereIsStaff(staffEmail)
                .stream()
                .map(h -> hackathonMapper.hackathonToAssignedHackathon(h, staffEmail))
                .toList();
    }

    @Override
    public List<HackathonResponse> getParticipatingHackathons(String user) {
        User userRepo = userRepository.findById(user).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Team team = userRepo.getTeam();
        if(team == null) {
            throw new IllegalArgumentException("Team not found");
        }

        List<Hackathon> hackathons = hackathonRepository.findAllByParticipatingTeam(team.getName());

        if(hackathons.isEmpty()) {
            throw new IllegalArgumentException("Team not in a hackathon");
        }

        return hackathons.stream().map(hackathonMapper::hackathonToHackathonResponse).toList();
    }

    private Hackathon getHackathon(Long hackathonId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElse(null);

        if(hackathon == null) {
            throw new IllegalArgumentException("Hackathon not found");
        }

        return hackathon;
    }

    private void checkStaffAlreadyPresent(List<String> emailList, Hackathon hackathon) {
        if(emailList.stream()
                .anyMatch(e -> e.equals(hackathon.getOrganizer().getEmail()) ||
                                      e.equals(hackathon.getJudge().getEmail()) ||
                                      hackathon.getMentors().stream()
                                              .anyMatch(m -> m.getEmail().equals(e)))) {
            throw new IllegalArgumentException("Staff already exists in hackathon");
        }
    }

    private void checkLogicDateOrder(CreateHackathonRequest request) {

        if (request.hackathonPeriod().endDate().isBefore(request.hackathonPeriod().startDate())) {
            throw new IllegalStateException("Dates order not valid");
        }

        if (request.subscriptionDeadline().isAfter(request.hackathonPeriod().startDate()) ||
                request.subscriptionDeadline().isEqual(request.hackathonPeriod().startDate())) {
            throw new IllegalStateException("Dates order not valid");
        }
    }

    private void checkSelfStaff(CreateHackathonRequest request) {
        if(request.organizerEmail().equals(request.judgeEmail())) {
            throw new IllegalStateException("Staff cannot be self");
        }
        if(request.mentorsEmails().stream().anyMatch(e -> e.equals(request.organizerEmail()))) {
            throw new IllegalStateException("Staff cannot be self");
        }
        if(request.mentorsEmails().stream().anyMatch(e -> e.equals(request.judgeEmail()))) {
            throw new IllegalStateException("judge cannot be also mentor");
        }
    }
}
