package unicam.hackhub.application.hackathon;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.staff.model.Judge;
import unicam.hackhub.domain.staff.model.Mentor;
import unicam.hackhub.domain.staff.model.Organizer;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.staff.repository.StaffRepository;
import unicam.hackhub.application.hackathon.request.CreateHackathonRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@Primary
public class CreateHackathonHandlerImpl implements CreateHackathonHandler {

    private final StaffRepository staffMemberRepo;
    private final HackathonRepository hackathonRepo;

    public CreateHackathonHandlerImpl(StaffRepository smr, HackathonRepository hr) {
        this.staffMemberRepo = smr;
        this.hackathonRepo = hr;
    }

    @Override
    public Hackathon createHackathon(CreateHackathonRequest request) {

        checkLogicDateOrder(request);

        Staff organizer = staffMemberRepo.findById(request.organizerEmail()).orElse(null);
        Staff judge = staffMemberRepo.findById(request.judgeEmail()).orElse(null);
        List<Staff> mentors = request.mentorsEmails().stream()
            .map(e -> staffMemberRepo.findById(e).orElse(null))
            .filter(Objects::nonNull)
            .toList();

        if(organizer == null || judge == null || mentors.size() != request.mentorsEmails().size()) {
            throw new IllegalArgumentException("Staffmember not found");
        }

        Hackathon hackathon = new Hackathon(
                request.name(),
                request.subscriptionDeadline(),
                request.hackathonPeriod(),
                request.maxTeamSize(),
                request.requirements(),
                request.prize(), organizer,
                judge,
                new HashSet<>(mentors));

        hackathonRepo.save(hackathon);
        
        return hackathon;
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
    
}
