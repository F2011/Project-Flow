package dhbw.swe.entities;

import static org.junit.jupiter.api.Assertions.assertThrows;

import dhbw.swe.services.ReservationService;
import dhbw.swe.valueobjects.Qualification;
import org.junit.jupiter.api.Test;

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

    @Test
    void reserveResource_employeeWithoutRequiredQualification_throwsException() {
        Employee employee = setup.createEmployeeWithQualification(Qualification.PYTHON);
        Project project = setup.createProjectWithRequiredQualification(Qualification.JAVA);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.reserveResource(employee, TestSetup.DURATION, project));
    }
}
