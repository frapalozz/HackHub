package code.java.src.application.hackathon;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import code.java.src.application.hackathon.request.CreateHackathonRequest;
import code.java.src.domain.hackathon.model.Hackathon;
import code.java.src.domain.hackathon.repository.HackathonRepository;
import code.java.src.domain.staffMember.model.Judge;
import code.java.src.domain.staffMember.model.Mentor;
import code.java.src.domain.staffMember.model.Organizer;
import code.java.src.domain.staffMember.repository.StaffMemberRepository;

public class CreateHackathonHandlerImpl implements CreateHackathonHandler {

    private StaffMemberRepository staffMemberRepo;
    private HackathonRepository hackathonRepo;

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
            .filter(m -> m != null)
            .toList();

        if(organizer == null || judge == null || mentors.size() != request.mentorsEmails().size()) {
            throw new IllegalArgumentException("Staffmember not found");
        }

        Hackathon hackathon = new Hackathon(request.name(), request.subscriptionDeadline(), request.hackathonPeriod(), request.maxTeamSize(), request.requirements(), request.prize(), organizer, judge, mentors.stream().collect(Collectors.toSet()));

        hackathonRepo.save(hackathon);
        
        return hackathon;
    }

    private void checkLogicDateOrder(CreateHackathonRequest request) {

        if (request.hackathonPeriod().endDate().isBefore(request.hackathonPeriod().startDate())) {
            throw new IllegalStateException("Dates order not valid");
        }

        if (request.subscriptionDeadline().isAfter(request.hackathonPeriod().startDate())) {
            throw new IllegalStateException("Dates order not valid");
        }
    }
    
}
