package unicam.hackhub.application.supportRequest;

import unicam.hackhub.domain.utils.TimeRange;

import java.time.LocalDate;
import java.util.List;

public interface CalendarService {

    List<TimeRange> getFreeSlots(String mentorEmail, LocalDate date);

    String requestSupport(String teamMember, Long hackathonId, String mentorEmail, TimeRange slot, LocalDate date);
}
