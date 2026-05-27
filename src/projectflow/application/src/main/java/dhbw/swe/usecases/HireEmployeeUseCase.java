package dhbw.swe.usecases;

import dhbw.swe.entities.Company;
import dhbw.swe.entities.Employee;
import dhbw.swe.ports.CompanyRepository;
import dhbw.swe.ports.ResourceRepository;
import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.TimeRange;
import java.util.UUID;

public class HireEmployeeUseCase {

    private final CompanyRepository companyRepository;
    private final ResourceRepository resourceRepository;

    public HireEmployeeUseCase(CompanyRepository companyRepository,
            ResourceRepository resourceRepository) {
        this.companyRepository = companyRepository;
        this.resourceRepository = resourceRepository;
    }

    public Employee execute(UUID companyId, String name, Money costsPerHour,
            TimeRange availability) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("company not found: " + companyId));
        Employee employee = new Employee(UUID.randomUUID(), name, costsPerHour, availability);
        company.addResource(employee);
        resourceRepository.save(employee);
        companyRepository.save(company);
        return employee;
    }
}
