package dhbw.swe.valueobjects;

import dhbw.swe.entities.Project;
import dhbw.swe.entities.Resource;

public final class Reservation implements Comparable<Reservation> {

    private final Resource resource;
    private final TimeRange timeRange;
    private final Project project;

    public Reservation(Resource resource, TimeRange timeRange, Project project) {
        this.resource = resource;
        this.timeRange = timeRange;
        this.project = project;
    }

    public Resource getResource() {
        return resource;
    }

    public Project getProject() {
        return project;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public int compareTo(Reservation other) {
        int startCompare = this.timeRange.getStart().compareTo(other.timeRange.getStart());
        if (startCompare == 0) {
            return this.timeRange.getEnd().compareTo(other.timeRange.getEnd());
        }
        return startCompare;
    }
}
