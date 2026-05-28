package dhbw.swe.plugins.web;

import dhbw.swe.entities.Company;
import dhbw.swe.ports.CompanyRepository;
import dhbw.swe.usecases.HireEmployeeUseCase;
import dhbw.swe.valueobjects.Money;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Currency;
import java.util.UUID;

@RestController
@RequestMapping("/companies")
@Tag(name = "Companies")
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository,
            HireEmployeeUseCase hireEmployeeUseCase) {
        this.companyRepository = companyRepository;
    }

    public record CreateCompanyRequest(String name, BigDecimal budgetAmount,
            String budgetCurrency) {
    }
    public record CompanyResponse(UUID id, String name, BigDecimal budgetAmount,
            String budgetCurrency) {
    }

    @PostMapping
    @Operation(summary = "Create a company")
    public ResponseEntity<CompanyResponse> create(@RequestBody CreateCompanyRequest req) {
        UUID id = UUID.randomUUID();
        Money budget = new Money(req.budgetAmount(), Currency.getInstance(req.budgetCurrency()));
        Company company = new Company(id, req.name(), budget);
        companyRepository.save(company);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
        return ResponseEntity.created(location).body(toResponse(company));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a company by ID")
    public ResponseEntity<CompanyResponse> get(@PathVariable UUID id) {
        return companyRepository.findById(id).map(c -> ResponseEntity.ok(toResponse(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    private CompanyResponse toResponse(Company c) {
        return new CompanyResponse(c.getId(), c.getName(), c.getBudget().getAmount(),
                c.getBudget().getCurrency().getCurrencyCode());
    }
}
