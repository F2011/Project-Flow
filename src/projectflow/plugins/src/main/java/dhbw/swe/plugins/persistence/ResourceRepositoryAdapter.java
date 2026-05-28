package dhbw.swe.plugins.persistence;

import dhbw.swe.entities.Employee;
import dhbw.swe.entities.Resource;
import dhbw.swe.plugins.persistence.entity.CompanyJpaEntity;
import dhbw.swe.plugins.persistence.entity.EmployeeJpaEntity;
import dhbw.swe.plugins.persistence.entity.ReservationJpaEntity;
import dhbw.swe.plugins.persistence.entity.ResourceJpaEntity;
import dhbw.swe.plugins.persistence.spring.CompanyJpaRepository;
import dhbw.swe.plugins.persistence.spring.ResourceJpaRepository;
import dhbw.swe.ports.ResourceRepository;
import dhbw.swe.valueobjects.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ResourceRepositoryAdapter implements ResourceRepository {

    private final ResourceJpaRepository jpa;
    private final CompanyJpaRepository companyJpa;

    public ResourceRepositoryAdapter(ResourceJpaRepository jpa, CompanyJpaRepository companyJpa) {
        this.jpa = jpa;
        this.companyJpa = companyJpa;
    }

    @Override
    public void save(Resource resource) {
        ResourceJpaEntity existing = jpa.findById(resource.getId()).orElse(null);
        CompanyJpaEntity company = existing != null ? existing.getCompany()
                : findCompanyForResource(resource.getId());
        ResourceJpaEntity entity = DomainMapper.toJpaResource(resource, company, existing);
        jpa.save(entity);
    }

    @Override
    public Optional<Resource> findById(UUID id) {
        return jpa.findById(id).map(e -> {
            Resource resource = DomainMapper.toDomainResource(e);
            for (ReservationJpaEntity re : e.getReservations()) {
                // Project stub — only id matters for equals/hashCode in Reservation
                var projectStub = DomainMapper.toDomainProject(re.getProject());
                Reservation reservation = new Reservation(resource,
                        DomainMapper.toTimeRange(re.getTimeStart(), re.getTimeEnd()), projectStub);
                resource.addReservation(reservation);
            }
            return resource;
        });
    }

    @Override
    public List<Employee> findAllEmployees() {
        return jpa.findAll().stream()
                .filter(e -> e instanceof EmployeeJpaEntity)
                .map(e -> (Employee) DomainMapper.toDomainResource(e))
                .toList();
    }

    private CompanyJpaEntity findCompanyForResource(UUID resourceId) {
        return companyJpa.findAll().stream()
                .filter(c -> c.getResources().stream().anyMatch(r -> r.getId().equals(resourceId)))
                .findFirst()
                .orElse(null);
    }
}
