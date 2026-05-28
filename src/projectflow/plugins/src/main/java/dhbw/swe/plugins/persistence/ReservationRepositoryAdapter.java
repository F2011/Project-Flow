package dhbw.swe.plugins.persistence;

import dhbw.swe.plugins.persistence.entity.ProjectJpaEntity;
import dhbw.swe.plugins.persistence.entity.ReservationJpaEntity;
import dhbw.swe.plugins.persistence.entity.ResourceJpaEntity;
import dhbw.swe.plugins.persistence.spring.ProjectJpaRepository;
import dhbw.swe.plugins.persistence.spring.ReservationJpaRepository;
import dhbw.swe.plugins.persistence.spring.ResourceJpaRepository;
import dhbw.swe.ports.ReservationRepository;
import dhbw.swe.valueobjects.Reservation;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final ReservationJpaRepository jpa;
    private final ResourceJpaRepository resourceJpa;
    private final ProjectJpaRepository projectJpa;

    public ReservationRepositoryAdapter(ReservationJpaRepository jpa,
            ResourceJpaRepository resourceJpa, ProjectJpaRepository projectJpa) {
        this.jpa = jpa;
        this.resourceJpa = resourceJpa;
        this.projectJpa = projectJpa;
    }

    @Override
    public void save(UUID id, Reservation reservation) {
        ResourceJpaEntity resourceEntity = resourceJpa.findById(reservation.getResource().getId())
                .orElseThrow(() -> new IllegalStateException(
                        "resource not found: " + reservation.getResource().getId()));
        ProjectJpaEntity projectEntity = projectJpa.findById(reservation.getProject().getId())
                .orElseThrow(() -> new IllegalStateException(
                        "project not found: " + reservation.getProject().getId()));

        ReservationJpaEntity entity = new ReservationJpaEntity(id, resourceEntity, projectEntity,
                reservation.getTimeRange().getStart(), reservation.getTimeRange().getEnd());
        jpa.save(entity);
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return jpa.findById(id).map(e -> {
            var resource = DomainMapper.toDomainResource(e.getResource());
            var project = DomainMapper.toDomainProject(e.getProject());
            return new Reservation(resource,
                    DomainMapper.toTimeRange(e.getTimeStart(), e.getTimeEnd()), project);
        });
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
