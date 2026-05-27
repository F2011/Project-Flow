package dhbw.swe.usecases;

import dhbw.swe.entities.Employee;
import dhbw.swe.ports.ResourceRepository;
import dhbw.swe.valueobjects.Qualification;
import java.util.UUID;

public class AddQualificationUseCase {

    private final ResourceRepository resourceRepository;

    public AddQualificationUseCase(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public void execute(UUID employeeId, Qualification qualification) {
        Employee employee = resourceRepository.findById(employeeId)
                .filter(r -> r instanceof Employee)
                .map(r -> (Employee) r)
                .orElseThrow(
                        () -> new IllegalArgumentException("employee not found: " + employeeId));
        employee.addQualification(qualification);
        resourceRepository.save(employee);
    }
}
