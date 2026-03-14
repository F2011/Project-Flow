package dhbw.swe.entities;

import dhbw.swe.valueobjects.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompanyTest {

    private TestSetup setup = new TestSetup();

    @Test
    void createProject_withinBudget() {
        Company company = setup.createCompanyWithBudget(Money.euro(100_000));

        setup.createProjectWithBudget(company, Money.euro(50_000));

        assertEquals(1, company.getProjects().size());
    }

    @Test
    void createProject_exceedingBudget() {
        Company company = setup.createCompanyWithBudget(Money.euro(100_000));

        assertThrows(IllegalArgumentException.class,
                () -> company.createProject("Too Expensive (fails)", Money.euro(200_000),
                        TestSetup.DURATION));
    }

    @Test
    void createProject_withExactBudget() {
        Company company = setup.createCompanyWithBudget(Money.euro(100_000));

        setup.createProjectWithBudget(company, Money.euro(100_000));

        assertEquals(1, company.getProjects().size());
    }

    @Test
    void createProject_withNegativeBudget() {
        Company company = setup.createCompanyWithBudget(Money.euro(100_000));

        assertThrows(IllegalArgumentException.class,
                () -> company.createProject("Negative Budget (fails)", Money.euro(-50_000),
                        TestSetup.DURATION));
    }
}
