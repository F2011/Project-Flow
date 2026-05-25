package dhbw.swe.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dhbw.swe.services.ReservationService;
import dhbw.swe.valueobjects.Reservation;
import org.junit.jupiter.api.Test;

public class ReservationTest {

    private TestSetup setup = new TestSetup();
    private ReservationService reservationService = new ReservationService();

    @Test
    void createReservation_exceedingResourceAvailability() {
        Resource resource = setup.createEmployeeWithAvailability(TestSetup.DURATION);
        Project project = setup.createProjectWithDuration(TestSetup.DURATION);

        assertThrows(IllegalArgumentException.class, () -> reservationService
                .reserveResource(resource, TestSetup.LONG_DURATION, project));
    }

    @Test
    void removeReservation_fromProject_removesCorrectly() {
        Project project = setup.createProjectWithDuration(TestSetup.DURATION);
        Employee employee = setup.createEmployeeWithAvailability(TestSetup.DURATION);

        Reservation r1 = new Reservation(employee, TestSetup.DURATION, project);
        Reservation r2 = new Reservation(employee, TestSetup.DURATION, project);

        project.addReservation(r1);
        assertEquals(1, project.getReservations().size());

        project.removeReservation(r2);

        assertEquals(0, project.getReservations().size(),
                "Removing an equal reservation instance must empty the set");
    }

    @Test
    void reserveResource_outsideProjectDuration_throwsException() {
        Project project = setup.createProjectWithDuration(TestSetup.DURATION);
        Employee employee = setup.createEmployeeWithAvailability(TestSetup.LONG_DURATION);

        assertThrows(IllegalArgumentException.class, () -> reservationService
                .reserveResource(employee, TestSetup.AFTER_DURATION, project));
    }
}
