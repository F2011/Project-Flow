package dhbw.swe.usecases;

import dhbw.swe.entities.Company;
import dhbw.swe.entities.Employee;
import dhbw.swe.entities.Project;
import dhbw.swe.valueobjects.Reservation;
import dhbw.swe.valueobjects.TimeRange;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenerateReportUseCaseTest {

    @Test
    void execute_returnsOnlyReservationsOverlappingTheGivenRange() {
        UseCaseTestSetup s = new UseCaseTestSetup();
        Company company = s.createCompany();
        Employee employee = s.createEmployee(company);
        Project project = s.createProject(company);
        BookResourceUseCase book = new BookResourceUseCase(s.projectRepo, s.resourceRepo,
                s.reservationRepo, s.reservationService);
        book.execute(project.getId(), employee.getId(), UseCaseTestSetup.RESERVATION_RANGE);

        GenerateReportUseCase report = new GenerateReportUseCase(s.resourceRepo);

        List<Reservation> inRange =
                report.execute(employee.getId(), UseCaseTestSetup.PROJECT_DURATION);
        assertEquals(1, inRange.size());
        assertEquals(project, inRange.get(0).getProject());

        TimeRange beforeReservation = new TimeRange(LocalDateTime.of(2026, 5, 1, 0, 0),
                LocalDateTime.of(2026, 5, 31, 23, 59));
        List<Reservation> outOfRange = report.execute(employee.getId(), beforeReservation);
        assertTrue(outOfRange.isEmpty());
    }
}
