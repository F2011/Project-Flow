package dhbw.swe.entities;

import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.Qualification;
import dhbw.swe.valueobjects.Reservation;
import dhbw.swe.valueobjects.TimeRange;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Project {

    private final UUID id;
    private String name;
    private Money budget;
    private final Set<Reservation> reservations;
    private final TimeRange duration;
    private final List<Project> subProjects;
    private final Set<Qualification> requiredQualifications;

    public Project(UUID id, String name, Money budget, TimeRange duration) {
        this.id = id;
        this.name = name;
        this.budget = budget;
        this.duration = duration;
        this.reservations = new HashSet<>();
        this.subProjects = new ArrayList<>();
        this.requiredQualifications = new HashSet<>();
    }

    public Project createSubProject(String name, Money budget, TimeRange duration) {
        if (this.subProjects.stream().map((Project p) -> p.getBudget()).reduce(budget, Money::add)
                .compareTo(this.budget) > 0) {
            throw new IllegalArgumentException("subproject budget exceeds project budget");
        }
        if (!this.duration.includes(duration)) {
            throw new IllegalArgumentException(
                    "subproject duration must be within project duration");
        }
        Project subProject = new Project(UUID.randomUUID(), name, budget, duration);
        subProjects.add(subProject);
        return subProject;
    }

    public void addReservation(Reservation reservation) {
        // TODO add checks
        reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }

    public boolean hasResource(Resource resource, LocalDateTime from, LocalDateTime to) {
        return reservations.stream().filter(r -> r.getResource().equals(resource))
                .noneMatch(r -> r.getTimeRange().overlaps(new TimeRange(from, to)));
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getBudget() {
        return budget;
    }

    public Set<Reservation> getReservations() {
        return Collections.unmodifiableSet(reservations);
    }

    public List<Project> getSubProjects() {
        return Collections.unmodifiableList(subProjects);
    }
}
