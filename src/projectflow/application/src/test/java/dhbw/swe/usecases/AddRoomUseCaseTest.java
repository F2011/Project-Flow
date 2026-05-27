package dhbw.swe.usecases;

import dhbw.swe.entities.Company;
import dhbw.swe.entities.Resource;
import dhbw.swe.entities.Room;
import dhbw.swe.valueobjects.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddRoomUseCaseTest {

    @Test
    void execute_addsRoomToCompanyAndRepository() {
        UseCaseTestSetup s = new UseCaseTestSetup();
        Company company = s.createCompany();
        AddRoomUseCase useCase = new AddRoomUseCase(s.companyRepo, s.resourceRepo);

        Room result = useCase.execute(company.getId(), "Lab", 20, Money.euro(30),
                "R42", UseCaseTestSetup.PROJECT_DURATION);

        Company savedCompany = s.companyRepo.findById(company.getId()).orElseThrow();
        Resource savedResource = s.resourceRepo.findById(result.getId()).orElseThrow();
        assertTrue(savedCompany.getResources().contains(result));
        assertInstanceOf(Room.class, savedResource);
        assertEquals("Lab", savedResource.getName());
    }
}
