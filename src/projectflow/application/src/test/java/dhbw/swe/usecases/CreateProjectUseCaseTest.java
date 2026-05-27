package dhbw.swe.usecases;

import dhbw.swe.entities.Company;
import dhbw.swe.entities.Project;
import dhbw.swe.valueobjects.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateProjectUseCaseTest {

    @Test
    void execute_createsProjectOnCompanyAndSavesIt() {
        UseCaseTestSetup s = new UseCaseTestSetup();
        Company company = s.createCompany();
        CreateProjectUseCase useCase = new CreateProjectUseCase(s.companyRepo, s.projectRepo);

        Project result = useCase.execute(company.getId(), "ProjectA", Money.euro(5_000),
                UseCaseTestSetup.PROJECT_DURATION);

        assertNotNull(result.getId());
        assertTrue(company.getProjects().contains(result));
        assertTrue(s.projectRepo.findById(result.getId()).isPresent());
    }
}
