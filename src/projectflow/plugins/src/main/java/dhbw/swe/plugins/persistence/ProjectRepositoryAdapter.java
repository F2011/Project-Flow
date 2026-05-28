package dhbw.swe.plugins.persistence;

import dhbw.swe.entities.Project;
import dhbw.swe.plugins.persistence.entity.CompanyJpaEntity;
import dhbw.swe.plugins.persistence.entity.ProjectJpaEntity;
import dhbw.swe.plugins.persistence.entity.ReservationJpaEntity;
import dhbw.swe.plugins.persistence.entity.ResourceJpaEntity;
import dhbw.swe.plugins.persistence.spring.CompanyJpaRepository;
import dhbw.swe.plugins.persistence.spring.ProjectJpaRepository;
import dhbw.swe.ports.ProjectRepository;
import dhbw.swe.valueobjects.Reservation;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProjectRepositoryAdapter implements ProjectRepository {

    private final ProjectJpaRepository jpa;
    private final CompanyJpaRepository companyJpa;

    public ProjectRepositoryAdapter(ProjectJpaRepository jpa, CompanyJpaRepository companyJpa) {
        this.jpa = jpa;
        this.companyJpa = companyJpa;
    }

    @Override
    public void save(Project project) {
        ProjectJpaEntity existing = jpa.findById(project.getId()).orElse(null);
        CompanyJpaEntity company = existing != null ? existing.getCompany()
                : findCompanyForProject(project.getId());
        ProjectJpaEntity entity = DomainMapper.toJpaProject(project, company, existing);
        jpa.save(entity);
    }

    @Override
    public Optional<Project> findById(UUID id) {
        return jpa.findWithDetailsById(id).map(pe -> {
            Project project = DomainMapper.toDomainProject(pe);

            // Build resource stubs for reservation reconstruction
            Map<UUID, dhbw.swe.entities.Resource> resourceStubs = new HashMap<>();
            for (ReservationJpaEntity re : pe.getReservations()) {
                ResourceJpaEntity re2 = re.getResource();
                resourceStubs.computeIfAbsent(re2.getId(), k -> DomainMapper.toDomainResource(re2));
            }

            for (ReservationJpaEntity re : pe.getReservations()) {
                var resource = resourceStubs.get(re.getResource().getId());
                Reservation reservation = new Reservation(resource,
                        DomainMapper.toTimeRange(re.getTimeStart(), re.getTimeEnd()), project);
                project.addReservation(reservation);
                resource.addReservation(reservation);
            }

            return project;
        });
    }

    private CompanyJpaEntity findCompanyForProject(UUID projectId) {
        return companyJpa.findAll().stream()
                .filter(c -> c.getProjects().stream().anyMatch(p -> p.getId().equals(projectId)))
                .findFirst()
                .orElse(null);
    }
}
