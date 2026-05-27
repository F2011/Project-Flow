package dhbw.swe.usecases;

import dhbw.swe.entities.Company;
import dhbw.swe.entities.Employee;
import dhbw.swe.valueobjects.Qualification;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddQualificationUseCaseTest {

    @Test
    void execute_addsQualificationToEmployee() {
        UseCaseTestSetup s = new UseCaseTestSetup();
        Company company = s.createCompany();
        Employee employee = s.createEmployee(company);
        AddQualificationUseCase useCase = new AddQualificationUseCase(s.resourceRepo);

        useCase.execute(employee.getId(), Qualification.JAVA);

        Employee saved = (Employee) s.resourceRepo.findById(employee.getId()).orElseThrow();
        assertTrue(saved.hasQualification(Qualification.JAVA));
    }
}
