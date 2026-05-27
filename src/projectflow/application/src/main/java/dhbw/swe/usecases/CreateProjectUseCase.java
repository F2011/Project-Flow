package dhbw.swe.usecases;

import dhbw.swe.entities.Company;
import dhbw.swe.entities.Project;
import dhbw.swe.ports.CompanyRepository;
import dhbw.swe.ports.ProjectRepository;
import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.TimeRange;
import java.util.UUID;

public class CreateProjectUseCase {

    private final CompanyRepository companyRepository;
    private final ProjectRepository projectRepository;

    public CreateProjectUseCase(CompanyRepository companyRepository,
            ProjectRepository projectRepository) {
        this.companyRepository = companyRepository;
        this.projectRepository = projectRepository;
    }

    public Project execute(UUID companyId, String name, Money budget, TimeRange duration) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("company not found: " + companyId));
        Project project = company.createProject(name, budget, duration);
        projectRepository.save(project);
        companyRepository.save(company);
        return project;
    }
}
