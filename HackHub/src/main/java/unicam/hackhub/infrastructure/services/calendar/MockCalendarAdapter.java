package unicam.hackhub.infrastructure.services.calendar;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import unicam.hackhub.application.dto.response.SupportRequestResponse;
import unicam.hackhub.application.supportRequest.CalendarHandler;
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
public class MockCalendarAdapter implements CalendarHandler {

    private final StaffRepository staffRepository;
    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;
    private final SupportRequestRepository supportRequestRepository;

    @Override
    public List<TimeRange> getFreeSlots(String mentorEmail, LocalDate date) {

        if(date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date should be in the future");
        }

        Staff mentor = getMentor(mentorEmail);

        List<SupportRequest> requests = supportRequestRepository.findAllBlockingRequests(mentorEmail, date);

        // Return all the free slots of the mentor for that day
        return freeSlots(requests, mentor);
    }

    @Override
    public String requestSupport(String teamMember, Long hackathonId, String mentorEmail, TimeRange slot, LocalDate date) {

        if(date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date should be in the future");
        }

        if(!slot.validSlot()) {
            throw new IllegalArgumentException("Slot is not valid");
        }

        // Get hackathon, mentor, team
        Hackathon hackathon = getHackathon(hackathonId);
        if(!hackathon.inProgress()) {
            throw new IllegalStateException("Hackathon not in progress");
        }
        Staff mentor = getMentor(mentorEmail);
        Team team = getUser(teamMember).getTeam();
        checkData(hackathon, mentor, team, slot);

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

    @Override
    public String acceptRequest(@NonNull String mentorEmail, @NonNull Long requestId, @NonNull String linkCall) {

        Staff mentor = getMentor(mentorEmail);
        SupportRequest request = getSupportRequest(requestId);

        if(!request.getMentor().getEmail().equals(mentor.getEmail())) {
            throw new IllegalArgumentException("Mentor does not belong to this request");
        }

        request.accept(linkCall);

        supportRequestRepository.save(request);

        return "Request accepted";
    }

    @Override
    public String declineRequest(String mentorEmail, Long requestId) {

        Staff mentor = getMentor(mentorEmail);
        SupportRequest request = getSupportRequest(requestId);

        if(!request.getMentor().getEmail().equals(mentor.getEmail())) {
            throw new IllegalArgumentException("Mentor does not belong to this request");
        }

        request.decline();

        supportRequestRepository.save(request);

        return "Request declined";
    }

    @Override
    public List<SupportRequestResponse> getSupportRequests(String staffEmail) {
        return supportRequestRepository
                .findAllWhereIsStaff(staffEmail).stream()
                .map(r -> new SupportRequestResponse(
                        r.getId(),
                        r.getTeam().getName(),
                        r.getHackathon().getId(),
                        r.getMentor().getEmail(),
                        r.getState().name(),
                        r.getDate(),
                        r.getTimeRange(),
                        r.getCallLink()
                )).toList();
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

    private SupportRequest getSupportRequest(Long requestId) {
        SupportRequest supportRequest = supportRequestRepository.findById(requestId).orElse(null);

        if(supportRequest == null) {
            throw new IllegalArgumentException("Support request not found");
        }

        return supportRequest;
    }

    private List<TimeRange> freeSlots(List<SupportRequest> requests, Staff mentor) {
        List<TimeRange> freeSlots = new ArrayList<>();

        if(mentor.getWorkingHours() == null) {
            return freeSlots;
        }

        TimeRange workRange = mentor.getWorkingHours();
        LocalTime start = workRange.getStartTime();
        LocalTime end = workRange.getEndTime();

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
            if(candidate.getStartTime().isBefore(reqRange.getEndTime()) && candidate.getEndTime().isAfter(reqRange.getStartTime())) {
                occupied = true;
                break;
            }
        }

        return occupied;
    }

    private void checkData(Hackathon hackathon, Staff mentor, Team team, TimeRange slot) {

        if(hackathon.getMentors().stream().noneMatch(m -> m.getEmail().equals(mentor.getEmail()))) {
            throw new IllegalArgumentException("Mentor not in hackathon");
        }
        if(team == null) {
            throw new IllegalArgumentException("User has no team");
        }
        if(hackathon.getTeams().stream().noneMatch(t -> t.getName().equals(team.getName()))) {
            throw new IllegalArgumentException("Team not in hackathon");
        }

        if (slot.getStartTime().isBefore(mentor.getWorkingHours().getStartTime()) ||
                slot.getEndTime().isAfter(mentor.getWorkingHours().getEndTime())) {
            throw new IllegalArgumentException("Slot outside working hours");
        }
    }
}
