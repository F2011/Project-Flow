package dhbw.swe.entities;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import dhbw.swe.services.ReservationService;

public class ReservationTest {
    private TestSetup setup = new TestSetup();
    private ReservationService reservationService = new ReservationService();

    @Test
    void createReservation_exceedingResourceAvailability() {
        Resource resource = setup.createEmployeeWithAvailability(TestSetup.DURATION);
        Project project = setup.createProjectWithDuration(TestSetup.DURATION);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.reserveResource(resource, TestSetup.LONG_DURATION, project));
    }
}
