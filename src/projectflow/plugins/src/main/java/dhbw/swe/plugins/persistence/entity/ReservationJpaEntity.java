package dhbw.swe.plugins.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class ReservationJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private ResourceJpaEntity resource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectJpaEntity project;

    @Column(name = "time_start", nullable = false)
    private LocalDateTime timeStart;

    @Column(name = "time_end", nullable = false)
    private LocalDateTime timeEnd;

    protected ReservationJpaEntity() {}

    public ReservationJpaEntity(UUID id, ResourceJpaEntity resource, ProjectJpaEntity project,
            LocalDateTime timeStart, LocalDateTime timeEnd) {
        this.id = id;
        this.resource = resource;
        this.project = project;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public UUID getId() { return id; }
    public ResourceJpaEntity getResource() { return resource; }
    public ProjectJpaEntity getProject() { return project; }
    public LocalDateTime getTimeStart() { return timeStart; }
    public LocalDateTime getTimeEnd() { return timeEnd; }
}
