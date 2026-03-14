package dhbw.swe.entities;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import dhbw.swe.valueobjects.Reservation;
import dhbw.swe.valueobjects.TimeRange;

public abstract class Resource {

    private UUID id;
    private String name;
    protected TimeRange availability;
    // ordered List of reservations for this resource, sorted by start time
    protected SortedSet<Reservation> reservations;

    protected Resource(UUID id, String name, TimeRange availability) {
        this.name = name;
        this.id = id;
        this.availability = availability;
        this.reservations = new TreeSet<Reservation>();
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return this.id;
    }

    public TimeRange getAvailability() {
        return availability;
    }

    public SortedSet<Reservation> getReservations() {
        return reservations;
    }

    public boolean isAvailableDuring(TimeRange tr) {
        if (!availability.includes(tr)) {
            return false;
        }

        for (Reservation reservation : reservations) {
            TimeRange rTimeRange = reservation.getTimeRange();
            if (!rTimeRange.getEnd().isAfter(tr.getStart())) {
                continue;
            }
            if (!rTimeRange.getStart().isBefore(tr.getEnd())) {
                break;
            }
            if (rTimeRange.overlaps(tr)) {
                return false;
            }
        }
        return true;
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Resource resource))
            return false;
        return id.equals(resource.id);
    }
}
