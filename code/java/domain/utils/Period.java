import java.time.LocalDate;

public record Period(LocalDate startDate, LocalDate endDate) {
     /**
     * Return true if the passed dateTime is within this Period, false otherwise
     * @param dateTime
     * @return true if dateTime is within this Period, false otherwise
     */
    public boolean isWithinPeriod(LocalDate dateTime) {
        return (dateTime.isEqual(startDate) || dateTime.isAfter(startDate)) &&
                (dateTime.isEqual(endDate) || dateTime.isBefore(endDate));
    }

    /**
     * Return true if the passed Period overlaps with this Period, false otherwise
     * @param other
     * @return true if the passed Period overlaps with this Period, false otherwise
     */
    public boolean overlapsWith(Period other) {
        return this.startDate.isBefore(other.endDate) && other.startDate.isBefore(this.endDate);
    }
}
