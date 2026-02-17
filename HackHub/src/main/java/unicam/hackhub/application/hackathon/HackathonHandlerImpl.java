package unicam.hackhub.application.hackathon;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import unicam.hackhub.application.payment.PaymentService;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.staff.repository.StaffRepository;

import java.util.List;

@Service
@Primary
@AllArgsConstructor
public class HackathonHandlerImpl implements HackathonHandler {

    private final HackathonRepository hackathonRepository;
    private final StaffRepository staffRepository;
    private final PaymentService paymentService;

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

        List<Staff> mentors = staffRepository.findAllById(mentorsList);

        if(mentors.size() != mentorsList.size()) {
            throw new IllegalArgumentException("Mentors not found");
        }

        hackathon.addMentors(mentors);

        return "Mentors added";
    }

    private Hackathon getHackathon(Long hackathonId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElse(null);

        if(hackathon == null) {
            throw new IllegalArgumentException("Hackathon not found");
        }

        return hackathon;
    }
}
