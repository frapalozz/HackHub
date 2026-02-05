package plainjava.src.domain.utils;

import java.time.LocalDate;

public record Period(LocalDate startDate, LocalDate endDate) {
     /**
     * Return true if the passed date is within this Period, false otherwise
     * @param date date to confront
     * @return true if dateTime is within this Period, false otherwise
     */
    public boolean isWithinPeriod(LocalDate date) {
        return (date.isEqual(startDate) || date.isAfter(startDate)) &&
                (date.isEqual(endDate) || date.isBefore(endDate));
    }

    /**
     * Return true if the passed Period overlaps with this Period, false otherwise
     * @param other other period
     * @return true if the passed Period overlaps with this Period, false otherwise
     */
    public boolean overlapsWith(Period other) {
        return this.startDate.isBefore(other.endDate) && other.startDate.isBefore(this.endDate);
    }
}
