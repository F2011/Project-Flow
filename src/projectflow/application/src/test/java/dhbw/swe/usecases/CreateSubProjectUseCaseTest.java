package dhbw.swe.usecases;

import dhbw.swe.entities.Company;
import dhbw.swe.entities.Project;
import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.TimeRange;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CreateSubProjectUseCaseTest {

    @Test
    void execute_addsSubProjectToParentAndSavesIt() {
        UseCaseTestSetup s = new UseCaseTestSetup();
        Company company = s.createCompany();
        Project parent = s.createProject(company);
        CreateSubProjectUseCase useCase = new CreateSubProjectUseCase(s.projectRepo);

        TimeRange subDuration = new TimeRange(
                LocalDateTime.of(2026, 6, 1, 8, 0),
                LocalDateTime.of(2026, 6, 15, 17, 0));
        Project sub = useCase.execute(parent.getId(), "SubTask", Money.euro(1_000), subDuration);

        assertNotNull(sub.getId());
        assertTrue(parent.getSubProjects().contains(sub));
        assertTrue(s.projectRepo.findById(sub.getId()).isPresent());
    }
}
