package dhbw.swe.plugins.web;

import dhbw.swe.entities.Employee;
import dhbw.swe.ports.ResourceRepository;
import dhbw.swe.usecases.AddQualificationUseCase;
import dhbw.swe.usecases.HireEmployeeUseCase;
import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.Qualification;
import dhbw.swe.valueobjects.TimeRange;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Employees")
public class EmployeeController {

    private final HireEmployeeUseCase hireEmployeeUseCase;
    private final AddQualificationUseCase addQualificationUseCase;
    private final ResourceRepository resourceRepository;

    public EmployeeController(HireEmployeeUseCase hireEmployeeUseCase,
            AddQualificationUseCase addQualificationUseCase,
            ResourceRepository resourceRepository) {
        this.hireEmployeeUseCase = hireEmployeeUseCase;
        this.addQualificationUseCase = addQualificationUseCase;
        this.resourceRepository = resourceRepository;
    }

    public record HireEmployeeRequest(String name, BigDecimal costsPerHourAmount,
            String costsPerHourCurrency, LocalDateTime availabilityStart,
            LocalDateTime availabilityEnd) {}

    public record EmployeeResponse(UUID id, String name, BigDecimal costsPerHourAmount,
            String costsPerHourCurrency, Set<String> qualifications) {}

    public record AddQualificationRequest(String qualification) {}

    @GetMapping("/employees")
    @Operation(summary = "Get all employees")
    public ResponseEntity<List<EmployeeResponse>> getAll() {
        List<EmployeeResponse> employees = resourceRepository.findAllEmployees().stream()
                .map(this::toResponse).toList();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees/{employeeId}")
    @Operation(summary = "Get an employee by ID")
    public ResponseEntity<EmployeeResponse> getById(@PathVariable UUID employeeId) {
        return resourceRepository.findById(employeeId)
                .filter(r -> r instanceof Employee)
                .map(r -> ResponseEntity.ok(toResponse((Employee) r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/companies/{companyId}/employees")
    @Operation(summary = "Hire an employee for a company")
    public ResponseEntity<EmployeeResponse> hire(@PathVariable UUID companyId,
            @RequestBody HireEmployeeRequest req) {
        Money costs = new Money(req.costsPerHourAmount(), Currency.getInstance(req.costsPerHourCurrency()));
        TimeRange availability = new TimeRange(req.availabilityStart(), req.availabilityEnd());
        Employee employee = hireEmployeeUseCase.execute(companyId, req.name(), costs, availability);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/employees/{id}").buildAndExpand(employee.getId()).toUri();
        return ResponseEntity.created(location).body(toResponse(employee));
    }

    @PostMapping("/employees/{employeeId}/qualifications")
    @Operation(summary = "Add a qualification to an employee")
    public ResponseEntity<Void> addQualification(@PathVariable UUID employeeId,
            @RequestBody AddQualificationRequest req) {
        addQualificationUseCase.execute(employeeId, Qualification.valueOf(req.qualification()));
        return ResponseEntity.noContent().build();
    }

    private EmployeeResponse toResponse(Employee e) {
        return new EmployeeResponse(e.getId(), e.getName(),
                e.getCostsPerHour().getAmount(),
                e.getCostsPerHour().getCurrency().getCurrencyCode(),
                e.getQualifications().stream().map(Qualification::name).collect(Collectors.toSet()));
    }
}
