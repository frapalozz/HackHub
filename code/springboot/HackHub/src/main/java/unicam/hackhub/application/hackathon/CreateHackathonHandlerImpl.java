package unicam.hackhub.application.hackathon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.staffMember.model.Judge;
import unicam.hackhub.domain.staffMember.model.Mentor;
import unicam.hackhub.domain.staffMember.model.Organizer;
import unicam.hackhub.domain.staffMember.repository.StaffMemberRepository;
import unicam.hackhub.application.hackathon.request.CreateHackathonRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@Primary
public class CreateHackathonHandlerImpl implements CreateHackathonHandler {

    private final StaffMemberRepository staffMemberRepo;
    private final HackathonRepository hackathonRepo;

    @Autowired
    public CreateHackathonHandlerImpl(StaffMemberRepository smr, HackathonRepository hr) {
        this.staffMemberRepo = smr;
        this.hackathonRepo = hr;
    }

    @Override
    public Hackathon createHackathon(CreateHackathonRequest request) {

        checkLogicDateOrder(request);

        Organizer organizer = (Organizer) staffMemberRepo.findById(request.organizerEmail());
        Judge judge = (Judge) staffMemberRepo.findById(request.judgeEmail());
        List<Mentor> mentors = request.mentorsEmails().stream()
            .map(e -> (Mentor) staffMemberRepo.findById(e))
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
