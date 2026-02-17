package unicam.hackhub.domain.utils;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Embeddable
@Getter
@NoArgsConstructor
public class TimeRange {

    private LocalTime startTime;
    private LocalTime endTime;

    public TimeRange(LocalTime startTime, LocalTime endTime) {

        if(startTime == null || endTime == null) {
            throw new IllegalArgumentException("Invalid time range");
        }
        if (startTime.equals(endTime)) {
            throw new IllegalArgumentException("from and to cannot be the same time");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("from and to cannot be after to");
        }
        if (startTime.getMinute() != 0 || startTime.getSecond() != 0 || endTime.getMinute() != 0 || endTime.getSecond() != 0) {
            throw new IllegalArgumentException("Only full hours are allowed");
        }

        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean validTimeRange() {
        if(startTime == null || endTime == null) {
            return false;
        }
        if (startTime.equals(endTime)) {
            return false;
        }
        if (startTime.isAfter(endTime)) {
            return false;
        }
        return startTime.getMinute() == 0 && startTime.getSecond() == 0 && endTime.getMinute() == 0 && endTime.getSecond() == 0;
    }

    public boolean validSlot() {
        if(!validTimeRange()) {
            return false;
        }

        return startTime.plusHours(1).equals(endTime);
    }
}
