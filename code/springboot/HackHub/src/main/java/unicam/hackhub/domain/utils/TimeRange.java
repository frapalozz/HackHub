package unicam.hackhub.domain.utils;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Embeddable
@Getter
@NoArgsConstructor
public class TimeRange {

    private LocalTime from;
    private LocalTime to;

    public TimeRange(LocalTime from, LocalTime to) {

        if(from == null || to == null) {
            throw new IllegalArgumentException("Invalid time range");
        }
        if (from.equals(to)) {
            throw new IllegalArgumentException("from and to cannot be the same time");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("from and to cannot be after to");
        }
        if (from.getMinute() != 0 || from.getSecond() != 0 || to.getMinute() != 0 || to.getSecond() != 0) {
            throw new IllegalArgumentException("Only full hours are allowed");
        }

        this.from = from;
        this.to = to;
    }

    public boolean validTimeRange() {
        if(from == null || to == null) {
            return false;
        }
        if (from.equals(to)) {
            return false;
        }
        if (from.isAfter(to)) {
            return false;
        }
        return from.getMinute() == 0 && from.getSecond() == 0 && to.getMinute() == 0 && to.getSecond() == 0;
    }

    public boolean validSlot() {
        if(!validTimeRange()) {
            return false;
        }

        return from.plusHours(1).equals(to);
    }
}
