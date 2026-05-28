package dhbw.swe.plugins.persistence;

import dhbw.swe.entities.Company;
import dhbw.swe.entities.Employee;
import dhbw.swe.entities.Project;
import dhbw.swe.entities.Resource;
import dhbw.swe.entities.Room;
import dhbw.swe.plugins.persistence.entity.*;
import dhbw.swe.valueobjects.Money;
import org.hibernate.Hibernate;
import dhbw.swe.valueobjects.Qualification;
import dhbw.swe.valueobjects.Reservation;
import dhbw.swe.valueobjects.TimeRange;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class DomainMapper {

    public static Money toMoney(BigDecimal amount, String currency) {
        return new Money(amount, Currency.getInstance(currency));
    }

    public static TimeRange toTimeRange(java.time.LocalDateTime start,
            java.time.LocalDateTime end) {
        return new TimeRange(start, end);
    }

    public static Resource toDomainResource(ResourceJpaEntity e) {
        e = (ResourceJpaEntity) Hibernate.unproxy(e);
        if (e instanceof EmployeeJpaEntity emp) {
            Set<Qualification> quals = emp.getQualifications().isEmpty()
                    ? EnumSet.noneOf(Qualification.class)
                    : emp.getQualifications().stream().map(Qualification::valueOf).collect(
                            Collectors.toCollection(() -> EnumSet.noneOf(Qualification.class)));
            return new Employee(e.getId(), e.getName(),
                    toMoney(e.getCostsPerHourAmount(), e.getCostsPerHourCurrency()),
                    toTimeRange(e.getAvailabilityStart(), e.getAvailabilityEnd()), quals);
        }
        if (e instanceof RoomJpaEntity room) {
            return new Room(e.getId(), e.getName(), room.getSize(),
                    toMoney(e.getCostsPerHourAmount(), e.getCostsPerHourCurrency()),
                    room.getRoomNumber(),
                    toTimeRange(e.getAvailabilityStart(), e.getAvailabilityEnd()));
        }
        throw new IllegalStateException("Unknown resource type: " + e.getClass());
    }

    public static Project toDomainProject(ProjectJpaEntity e) {
        Project project = new Project(e.getId(), e.getName(),
                toMoney(e.getBudgetAmount(), e.getBudgetCurrency()),
                toTimeRange(e.getDurationStart(), e.getDurationEnd()));
        e.getRequiredQualifications()
                .forEach(q -> project.addRequiredQualification(Qualification.valueOf(q)));
        return project;
    }

    public static Project toDomainProjectFull(ProjectJpaEntity pe,
            Map<UUID, Resource> resourceStubs) {
        Project project = toDomainProject(pe);

        for (ReservationJpaEntity re : pe.getReservations()) {
            Resource res = resourceStubs.get(re.getResource().getId());
            if (res != null) {
                Reservation reservation = new Reservation(res,
                        toTimeRange(re.getTimeStart(), re.getTimeEnd()), project);
                project.addReservation(reservation);
                res.addReservation(reservation);
            }
        }

        for (ProjectJpaEntity sub : pe.getSubProjects()) {
            toDomainProjectFull(sub, resourceStubs);
        }

        return project;
    }

    public static Company toDomainCompany(CompanyJpaEntity e) {
        Company company = new Company(e.getId(), e.getName(),
                toMoney(e.getBudgetAmount(), e.getBudgetCurrency()));

        for (ResourceJpaEntity re : e.getResources()) {
            company.addResource(toDomainResource(re));
        }

        return company;
    }

    public static CompanyJpaEntity toJpaCompany(Company c, CompanyJpaEntity existing) {
        if (existing == null) {
            return new CompanyJpaEntity(c.getId(), c.getName(), c.getBudget().getAmount(),
                    c.getBudget().getCurrency().getCurrencyCode());
        }
        existing.setName(c.getName());
        existing.setBudgetAmount(c.getBudget().getAmount());
        existing.setBudgetCurrency(c.getBudget().getCurrency().getCurrencyCode());
        return existing;
    }

    public static ResourceJpaEntity toJpaResource(Resource resource, CompanyJpaEntity companyEntity,
            ResourceJpaEntity existing) {
        if (resource instanceof Employee emp) {
            EmployeeJpaEntity e = existing instanceof EmployeeJpaEntity ex ? ex
                    : new EmployeeJpaEntity(emp.getId(), emp.getName(),
                            emp.getAvailability().getStart(), emp.getAvailability().getEnd(),
                            emp.getCostsPerHour().getAmount(),
                            emp.getCostsPerHour().getCurrency().getCurrencyCode(), companyEntity);
            e.setCompany(companyEntity);
            e.setQualifications(emp.getQualifications().stream().map(Qualification::name)
                    .collect(Collectors.toList()));
            return e;
        }
        if (resource instanceof Room room) {
            RoomJpaEntity r = existing instanceof RoomJpaEntity ex ? ex
                    : new RoomJpaEntity(room.getId(), room.getName(),
                            room.getAvailability().getStart(), room.getAvailability().getEnd(),
                            room.getCostsPerHour().getAmount(),
                            room.getCostsPerHour().getCurrency().getCurrencyCode(), companyEntity,
                            room.getSize(), room.getRoomNumber());
            r.setCompany(companyEntity);
            return r;
        }
        throw new IllegalStateException("Unknown resource type: " + resource.getClass());
    }

    public static ProjectJpaEntity toJpaProject(Project project, CompanyJpaEntity companyEntity,
            ProjectJpaEntity existing) {
        if (existing == null) {
            return new ProjectJpaEntity(project.getId(), project.getName(),
                    project.getBudget().getAmount(),
                    project.getBudget().getCurrency().getCurrencyCode(),
                    project.getDuration().getStart(), project.getDuration().getEnd(),
                    companyEntity);
        }
        existing.setBudgetAmount(project.getBudget().getAmount());
        existing.setRequiredQualifications(project.getRequiredQualifications().stream()
                .map(Qualification::name).collect(Collectors.toList()));
        return existing;
    }
}
