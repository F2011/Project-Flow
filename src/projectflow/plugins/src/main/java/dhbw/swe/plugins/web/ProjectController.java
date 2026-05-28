package dhbw.swe.plugins.web;

import dhbw.swe.entities.Project;
import dhbw.swe.usecases.CreateProjectUseCase;
import dhbw.swe.usecases.CreateSubProjectUseCase;
import dhbw.swe.valueobjects.Money;
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
import java.util.UUID;

@RestController
@Tag(name = "Projects")
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final CreateSubProjectUseCase createSubProjectUseCase;

    public ProjectController(CreateProjectUseCase createProjectUseCase,
            CreateSubProjectUseCase createSubProjectUseCase) {
        this.createProjectUseCase = createProjectUseCase;
        this.createSubProjectUseCase = createSubProjectUseCase;
    }

    public record CreateProjectRequest(String name, BigDecimal budgetAmount, String budgetCurrency,
            LocalDateTime durationStart, LocalDateTime durationEnd) {}

    public record ProjectResponse(UUID id, String name, BigDecimal budgetAmount,
            String budgetCurrency, LocalDateTime durationStart, LocalDateTime durationEnd) {}

    @PostMapping("/companies/{companyId}/projects")
    @Operation(summary = "Create a project for a company")
    public ResponseEntity<ProjectResponse> createProject(@PathVariable UUID companyId,
            @RequestBody CreateProjectRequest req) {
        Money budget = new Money(req.budgetAmount(), Currency.getInstance(req.budgetCurrency()));
        TimeRange duration = new TimeRange(req.durationStart(), req.durationEnd());
        Project project = createProjectUseCase.execute(companyId, req.name(), budget, duration);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/projects/{id}").buildAndExpand(project.getId()).toUri();
        return ResponseEntity.created(location).body(toResponse(project));
    }

    @PostMapping("/projects/{parentId}/subprojects")
    @Operation(summary = "Create a sub-project under a parent project")
    public ResponseEntity<ProjectResponse> createSubProject(@PathVariable UUID parentId,
            @RequestBody CreateProjectRequest req) {
        Money budget = new Money(req.budgetAmount(), Currency.getInstance(req.budgetCurrency()));
        TimeRange duration = new TimeRange(req.durationStart(), req.durationEnd());
        Project sub = createSubProjectUseCase.execute(parentId, req.name(), budget, duration);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/projects/{id}").buildAndExpand(sub.getId()).toUri();
        return ResponseEntity.created(location).body(toResponse(sub));
    }

    private ProjectResponse toResponse(Project p) {
        return new ProjectResponse(p.getId(), p.getName(),
                p.getBudget().getAmount(), p.getBudget().getCurrency().getCurrencyCode(),
                p.getDuration().getStart(), p.getDuration().getEnd());
    }
}
