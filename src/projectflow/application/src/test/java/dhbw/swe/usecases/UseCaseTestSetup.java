package dhbw.swe.usecases;

import dhbw.swe.entities.Company;
import dhbw.swe.entities.Employee;
import dhbw.swe.entities.Project;
import dhbw.swe.entities.Resource;
import dhbw.swe.entities.Room;
import dhbw.swe.ports.CompanyRepository;
import dhbw.swe.ports.ProjectRepository;
import dhbw.swe.ports.ReservationRepository;
import dhbw.swe.ports.ResourceRepository;
import dhbw.swe.services.ReservationService;
import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.Reservation;
import dhbw.swe.valueobjects.TimeRange;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

class UseCaseTestSetup {

    static final LocalDateTime START = LocalDateTime.of(2026, 6, 1, 8, 0);
    static final LocalDateTime END = LocalDateTime.of(2026, 6, 30, 17, 0);
    static final TimeRange PROJECT_DURATION = new TimeRange(START, END);
    static final TimeRange RESERVATION_RANGE = new TimeRange(LocalDateTime.of(2026, 6, 10, 9, 0),
            LocalDateTime.of(2026, 6, 10, 11, 0));

    final CompanyRepository companyRepo = new InMemoryCompanyRepo();
    final ProjectRepository projectRepo = new InMemoryProjectRepo();
    final ResourceRepository resourceRepo = new InMemoryResourceRepo();
    final ReservationRepository reservationRepo = new InMemoryReservationRepo();
    final ReservationService reservationService = new ReservationService();

    Company createCompany() {
        Company company = new Company(UUID.randomUUID(), "Acme", Money.euro(100_000));
        companyRepo.save(company);
        return company;
    }

    Employee createEmployee(Company company) {
        Employee emp = new Employee(UUID.randomUUID(), "Alice", Money.euro(50), PROJECT_DURATION);
        company.addResource(emp);
        resourceRepo.save(emp);
        companyRepo.save(company);
        return emp;
    }

    Room createRoom(Company company) {
        Room room =
                new Room(UUID.randomUUID(), "Lab", 10, Money.euro(20), "R101", PROJECT_DURATION);
        company.addResource(room);
        resourceRepo.save(room);
        companyRepo.save(company);
        return room;
    }

    Project createProject(Company company) {
        Project project = company.createProject("ProjectA", Money.euro(10_000), PROJECT_DURATION);
        projectRepo.save(project);
        companyRepo.save(company);
        return project;
    }

    static class InMemoryCompanyRepo implements CompanyRepository {
        final Map<UUID, Company> store = new HashMap<>();

        public void save(Company c) {
            store.put(c.getId(), c);
        }

        public Optional<Company> findById(UUID id) {
            return Optional.ofNullable(store.get(id));
        }
    }

    static class InMemoryProjectRepo implements ProjectRepository {
        final Map<UUID, Project> store = new HashMap<>();

        public void save(Project p) {
            store.put(p.getId(), p);
        }

        public Optional<Project> findById(UUID id) {
            return Optional.ofNullable(store.get(id));
        }
    }

    static class InMemoryResourceRepo implements ResourceRepository {
        final Map<UUID, Resource> store = new HashMap<>();

        public void save(Resource r) {
            store.put(r.getId(), r);
        }

        public Optional<Resource> findById(UUID id) {
            return Optional.ofNullable(store.get(id));
        }
    }

    static class InMemoryReservationRepo implements ReservationRepository {
        final Map<UUID, Reservation> store = new HashMap<>();

        public void save(UUID id, Reservation r) {
            store.put(id, r);
        }

        public Optional<Reservation> findById(UUID id) {
            return Optional.ofNullable(store.get(id));
        }

        public void deleteById(UUID id) {
            store.remove(id);
        }
    }
}
