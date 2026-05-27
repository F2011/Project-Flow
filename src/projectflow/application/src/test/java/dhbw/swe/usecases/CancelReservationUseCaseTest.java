package dhbw.swe.usecases;

import dhbw.swe.entities.Company;
import dhbw.swe.entities.Employee;
import dhbw.swe.entities.Project;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CancelReservationUseCaseTest {

    @Test
    void execute_removesReservationFromProjectResourceAndRepository() {
        UseCaseTestSetup s = new UseCaseTestSetup();
        Company company = s.createCompany();
        Employee employee = s.createEmployee(company);
        Project project = s.createProject(company);
        BookResourceUseCase book = new BookResourceUseCase(
                s.projectRepo, s.resourceRepo, s.reservationRepo, s.reservationService);
        UUID reservationId = book.execute(project.getId(), employee.getId(),
                UseCaseTestSetup.RESERVATION_RANGE);

        CancelReservationUseCase cancel = new CancelReservationUseCase(
                s.reservationRepo, s.resourceRepo, s.projectRepo, s.reservationService);
        cancel.execute(reservationId);

        assertTrue(s.reservationRepo.findById(reservationId).isEmpty());
        Project savedProject = s.projectRepo.findById(project.getId()).orElseThrow();
        Employee savedEmployee = (Employee) s.resourceRepo.findById(employee.getId()).orElseThrow();
        assertTrue(savedProject.getReservations().isEmpty());
        assertTrue(savedEmployee.getReservations().isEmpty());
    }
}
