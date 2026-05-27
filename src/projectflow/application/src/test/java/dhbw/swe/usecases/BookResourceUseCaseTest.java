package dhbw.swe.usecases;

import dhbw.swe.entities.Company;
import dhbw.swe.entities.Employee;
import dhbw.swe.entities.Project;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookResourceUseCaseTest {

    @Test
    void execute_createsReservationOnProjectAndResource() {
        UseCaseTestSetup s = new UseCaseTestSetup();
        Company company = s.createCompany();
        Employee employee = s.createEmployee(company);
        Project project = s.createProject(company);
        BookResourceUseCase useCase = new BookResourceUseCase(
                s.projectRepo, s.resourceRepo, s.reservationRepo, s.reservationService);

        UUID reservationId = useCase.execute(project.getId(), employee.getId(),
                UseCaseTestSetup.RESERVATION_RANGE);

        assertNotNull(reservationId);
        assertTrue(s.reservationRepo.findById(reservationId).isPresent());
        assertEquals(1, project.getReservations().size());
        assertEquals(1, employee.getReservations().size());
    }
}
