package dhbw.swe.usecases;

import dhbw.swe.entities.Company;
import dhbw.swe.entities.Employee;
import dhbw.swe.entities.Resource;
import dhbw.swe.valueobjects.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HireEmployeeUseCaseTest {

    @Test
    void execute_addsEmployeeToCompanyAndRepository() {
        UseCaseTestSetup s = new UseCaseTestSetup();
        Company company = s.createCompany();
        HireEmployeeUseCase useCase = new HireEmployeeUseCase(s.companyRepo, s.resourceRepo);

        Employee result = useCase.execute(company.getId(), "Bob", Money.euro(80),
                UseCaseTestSetup.PROJECT_DURATION);

        Company savedCompany = s.companyRepo.findById(company.getId()).orElseThrow();
        Resource savedResource = s.resourceRepo.findById(result.getId()).orElseThrow();
        assertTrue(savedCompany.getResources().contains(result));
        assertInstanceOf(Employee.class, savedResource);
        assertEquals("Bob", savedResource.getName());
    }
}
