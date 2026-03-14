package dhbw.swe.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import dhbw.swe.services.ReservationService;

public class EmployeeTest {
    private TestSetup setup = new TestSetup();
    private ReservationService reservationService = new ReservationService();

    @Test
    void createOverlappigReservations() {
        Employee employee = setup.createEmployeeWithAvailability(TestSetup.DURATION);
        Project project = setup.createProjectWithDuration(TestSetup.DURATION);
        reservationService.reserveResource(employee, TestSetup.DURATION, project);
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.reserveResource(employee, TestSetup.DURATION, project));
    }
}
