package dhbw.swe.entities;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.Reservation;
import dhbw.swe.valueobjects.TimeRange;
import dhbw.swe.valueobjects.Qualification;

public class ProjectTest {
    private TestSetup setup = new TestSetup();

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
}
