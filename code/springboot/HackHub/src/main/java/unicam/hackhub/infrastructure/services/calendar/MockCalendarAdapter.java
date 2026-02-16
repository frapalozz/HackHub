package unicam.hackhub.infrastructure.services.calendar;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.application.supportRequest.CalendarService;
import unicam.hackhub.domain.hackathon.model.Hackathon;
import unicam.hackhub.domain.hackathon.repository.HackathonRepository;
import unicam.hackhub.domain.staff.model.Staff;
import unicam.hackhub.domain.staff.repository.StaffRepository;
import unicam.hackhub.domain.support.model.RequestState;
import unicam.hackhub.domain.support.model.SupportRequest;
import unicam.hackhub.domain.support.repository.SupportRequestRepository;
import unicam.hackhub.domain.team.model.Team;
import unicam.hackhub.domain.user.model.User;
import unicam.hackhub.domain.user.repository.UserRepository;
import unicam.hackhub.domain.utils.TimeRange;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
@AllArgsConstructor
public class MockCalendarAdapter implements CalendarService {

    private final StaffRepository staffRepository;
    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;
    private final SupportRequestRepository supportRequestRepository;

    @Override
    public List<TimeRange> getFreeSlots(String mentorEmail, LocalDate date) {

        Staff mentor = getMentor(mentorEmail);

        List<SupportRequest> requests = supportRequestRepository.findAllBlockingRequests(mentorEmail, date);

        // Return all the free slots of the mentor for that day
        return freeSlots(requests, mentor);
    }

    @Override
    public String requestSupport(String teamMember, Long hackathonId, String mentorEmail, TimeRange slot, LocalDate date) {

        if(!slot.validSlot()) {
            throw new IllegalArgumentException("Slot is not valid");
        }

        // Get hackathon, mentor, team
        Hackathon hackathon = getHackathon(hackathonId);
        Staff mentor = getMentor(mentorEmail);
        if(hackathon.getMentors().stream().noneMatch(m -> m.getEmail().equals(mentor.getEmail()))) {
            throw new IllegalArgumentException("Mentor not in hackathon");
        }
        Team team = getUser(teamMember).getTeam();
        if(team == null) {
            throw new IllegalArgumentException("User has no team");
        }
        if(hackathon.getTeams().stream().noneMatch(t -> t.getName().equals(team.getName()))) {
            throw new IllegalArgumentException("Team not in hackathon");
        }

        if (slot.getFrom().isBefore(mentor.getTimeRange().getFrom()) ||
                slot.getTo().isAfter(mentor.getTimeRange().getTo())) {
            throw new IllegalArgumentException("Slot outside working hours");
        }

        List<SupportRequest> requests = supportRequestRepository.findAllBlockingRequests(mentorEmail, date);

        // Check if slot is occupied
        if(occupiedSlot(requests, slot)) {
            throw new IllegalArgumentException("Slot already occupied");
        }

        SupportRequest request = SupportRequest.builder()
                .team(team)
                .mentor(mentor)
                .hackathon(hackathon)
                .state(RequestState.PENDING)
                .date(date)
                .timeRange(slot)
                .build();

        supportRequestRepository.save(request);

        return "Request created";
    }

    private Staff getMentor(String mentorEmail) {
        Staff mentor = staffRepository.findById(mentorEmail).orElse(null);

        if(mentor == null) {
            throw new IllegalArgumentException("Mentor not found");
        }

        return mentor;
    }

    private Hackathon getHackathon(Long hackathonId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElse(null);

        if(hackathon == null) {
            throw new IllegalArgumentException("Hackathon not found");
        }

        return hackathon;
    }

    private User getUser(String email) {
        User user = userRepository.findById(email).orElse(null);

        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return user;
    }

    private List<TimeRange> freeSlots(List<SupportRequest> requests, Staff mentor) {
        List<TimeRange> freeSlots = new ArrayList<>();

        if(mentor.getTimeRange() == null) {
            return freeSlots;
        }

        TimeRange workRange = mentor.getTimeRange();
        LocalTime start = workRange.getFrom();
        LocalTime end = workRange.getTo();

        LocalTime slotStart = start;
        while (slotStart.plusHours(1).isBefore(end) || slotStart.plusHours(1).equals(end)) {
            LocalTime slotEnd = slotStart.plusHours(1);
            TimeRange candidate = new TimeRange(slotStart, slotEnd);

            // Verifica se lo slot candidato è occupato da qualche richiesta
            if(!occupiedSlot(requests, candidate)) {
                freeSlots.add(candidate);
            }

            slotStart = slotEnd;
        }

        return freeSlots;
    }

    private boolean occupiedSlot(List<SupportRequest> requests, TimeRange candidate) {
        boolean occupied = false;

        for (SupportRequest req : requests) {
            TimeRange reqRange = req.getTimeRange();

            // Condizione di sovrapposizione (intervalli [start, end] )
            if(candidate.getFrom().isBefore(reqRange.getTo()) && candidate.getTo().isAfter(reqRange.getFrom())) {
                occupied = true;
                break;
            }
        }

        return occupied;
    }
}
