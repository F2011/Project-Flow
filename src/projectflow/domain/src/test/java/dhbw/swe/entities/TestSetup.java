package dhbw.swe.entities;

import java.time.LocalDateTime;
import java.util.UUID;
import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.Qualification;
import dhbw.swe.valueobjects.TimeRange;

public class TestSetup {

    private static final LocalDateTime START = LocalDateTime.of(2026, 1, 1, 0, 0);
    private static final LocalDateTime END = LocalDateTime.of(2026, 1, 1, 23, 59);
    public static final TimeRange DURATION = new TimeRange(START, END);
    public static final TimeRange LONG_DURATION =
            new TimeRange(START.minusHours(1), END.plusHours(1));
    public static final TimeRange AFTER_DURATION =
            new TimeRange(END.plusHours(1), END.plusHours(9));

    public Company createCompanyWithBudget(Money budget) {
        return new Company(UUID.randomUUID(), "Test AG", budget);
    }

    public Project createProjectWithBudget(Company company, Money budget) {
        return company.createProject("TestProject", budget, DURATION);
    }

    public Project createProjectWithBudget(Money budget) {
        Company company = createCompanyWithBudget(Money.euro(100_000));
        return createProjectWithBudget(company, budget);
    }

    public Project createProjectWithDuration(TimeRange duration) {
        Company company = createCompanyWithBudget(Money.euro(100_000));
        return company.createProject("TestProject", Money.euro(10_000), duration);
    }

    public Employee createEmployeeWithQualification(Qualification qualification) {
        Employee employee =
                new Employee(UUID.randomUUID(), "Test Employee", Money.euro(0), DURATION);
        employee.addQualification(qualification);
        return employee;
    }

    public Employee createEmployeeWithAvailability(TimeRange availability) {
        return new Employee(UUID.randomUUID(), "Test Employee", Money.euro(0), availability);
    }

    public Employee createEmployeeWithCostsPerHour(Money costsPerHour) {
        return new Employee(UUID.randomUUID(), "Test Employee", costsPerHour, DURATION);
    }

    public Project createProjectWithRequiredQualification(Qualification qualification) {
        Company company = createCompanyWithBudget(Money.euro(100_000));
        Project project = company.createProject("TestProject", Money.euro(10_000), DURATION);
        project.addRequiredQualification(qualification);
        return project;
    }
}
