package dhbw.swe.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dhbw.swe.services.ReservationService;
import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.Qualification;
import dhbw.swe.valueobjects.Reservation;
import org.junit.jupiter.api.Test;

public class ProjectTest {

    private TestSetup setup = new TestSetup();
    private ReservationService reservationService = new ReservationService();

    @Test
    void createSubProject_exceedingBudget() {
        Project project = setup.createProjectWithBudget(Money.euro(50_000));

        assertThrows(IllegalArgumentException.class, () -> project
                .createSubProject("Too Expensive (fails)", Money.euro(60_000), TestSetup.DURATION));
    }

    @Test
    void createSubProject_withExactBudget() {
        Project project = setup.createProjectWithBudget(Money.euro(50_000));

        project.createSubProject("Exact Budget (succeeds)", Money.euro(50_000), TestSetup.DURATION);

        assertEquals(1, project.getSubProjects().size());
    }

    @Test
    void createSubProject_exceedingDuration() {
        Project project = setup.createProjectWithBudget(Money.euro(50_000));

        assertThrows(IllegalArgumentException.class, () -> project
                .createSubProject("Too Long (fails)", Money.euro(10_000), TestSetup.LONG_DURATION));
    }

    @Test
    void createSubProject_withExactDuration() {
        Project project = setup.createProjectWithBudget(Money.euro(50_000));

        project.createSubProject("Within Duration (succeeds)", Money.euro(10_000),
                TestSetup.DURATION);

        assertEquals(1, project.getSubProjects().size());
    }

    @Test
    void reserveResource() {
        Project project = setup.createProjectWithBudget(Money.euro(50_000));
        Resource resource = setup.createEmployeeWithQualification(Qualification.JAVA);
        Reservation reservation = new Reservation(resource, TestSetup.DURATION, project);
        project.addReservation(reservation);
        assertEquals(1, project.getReservations().size());
    }

    @Test
    void createSubProject_withExistingSubProject_budgetCheckAccountsForExistingSubs() {
        Project project = setup.createProjectWithBudget(Money.euro(50_000));
        project.createSubProject("First Sub", Money.euro(30_000), TestSetup.DURATION);

        assertThrows(IllegalArgumentException.class,
                () -> project.createSubProject("Second Sub Over Budget (fails)", Money.euro(25_000),
                        TestSetup.DURATION));
    }

    @Test
    void hasResource_returnsFalse_whenResourceIsNotBooked() {
        Project project = setup.createProjectWithBudget(Money.euro(50_000));
        Employee employee = setup.createEmployeeWithAvailability(TestSetup.DURATION);

        boolean result = project.hasResource(employee, TestSetup.DURATION.getStart(),
                TestSetup.DURATION.getEnd());

        assertFalse(result, "hasResource must return false when resource has no reservation");
    }

    @Test
    void hasResource_returnsTrue_whenResourceIsBooked() {
        Project project = setup.createProjectWithBudget(Money.euro(50_000));
        Employee employee = setup.createEmployeeWithAvailability(TestSetup.DURATION);
        Reservation reservation = new Reservation(employee, TestSetup.DURATION, project);
        project.addReservation(reservation);

        boolean result = project.hasResource(employee, TestSetup.DURATION.getStart(),
                TestSetup.DURATION.getEnd());

        assertTrue(result, "hasResource must return true when resource is booked in that range");
    }

    @Test
    void reserveResource_whenTotalCostExceedsProjectBudget_throwsException() {
        Project project = setup.createProjectWithBudget(Money.euro(100));
        Employee employee = setup.createEmployeeWithCostsPerHour(Money.euro(10));

        // 10 €/h × 23.98 h ≈ 239.80 € — exceeds the 100 € project budget
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.reserveResource(employee, TestSetup.DURATION, project));
    }

    @Test
    void addRequiredQualification_isReflectedInGetter() {
        Project project = setup.createProjectWithBudget(Money.euro(50_000));

        project.addRequiredQualification(Qualification.JAVA);

        assertTrue(project.getRequiredQualifications().contains(Qualification.JAVA));
    }
}
