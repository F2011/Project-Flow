package dhbw.swe.usecases;

import dhbw.swe.entities.Project;
import dhbw.swe.ports.ProjectRepository;
import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.TimeRange;
import java.util.UUID;

public class CreateSubProjectUseCase {

    private final ProjectRepository projectRepository;

    public CreateSubProjectUseCase(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project execute(UUID parentProjectId, String name, Money budget, TimeRange duration) {
        Project parent = projectRepository.findById(parentProjectId)
                .orElseThrow(
                        () -> new IllegalArgumentException("project not found: " + parentProjectId));
        Project subProject = parent.createSubProject(name, budget, duration);
        projectRepository.save(subProject);
        projectRepository.save(parent);
        return subProject;
    }
}
