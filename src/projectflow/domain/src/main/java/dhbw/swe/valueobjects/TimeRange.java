package dhbw.swe.valueobjects;

import java.time.LocalDateTime;

public final class TimeRange {

    private final LocalDateTime start;
    private final LocalDateTime end;

    public TimeRange(LocalDateTime start, LocalDateTime end) {
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("invalid time range");
        }
        this.start = start;
        this.end = end;
    }

    public boolean overlaps(TimeRange other) {
        return this.start.isBefore(other.end) && other.start.isBefore(this.end);
    }

    public boolean includes(TimeRange other) {
        return !other.start.isBefore(this.start) && !other.end.isAfter(this.end);
    }

    public java.time.Duration getDuration() {
        return java.time.Duration.between(start, end);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
