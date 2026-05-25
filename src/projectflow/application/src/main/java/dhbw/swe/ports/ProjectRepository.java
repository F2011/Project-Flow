package dhbw.swe.ports;

import dhbw.swe.entities.Project;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository {
    void save(Project project);
    Optional<Project> findById(UUID id);
}
