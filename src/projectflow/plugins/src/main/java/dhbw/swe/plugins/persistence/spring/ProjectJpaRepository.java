package dhbw.swe.plugins.persistence.spring;

import dhbw.swe.plugins.persistence.entity.ProjectJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ProjectJpaRepository extends JpaRepository<ProjectJpaEntity, UUID> {

    @EntityGraph(attributePaths = {"reservations", "subProjects", "requiredQualifications"})
    Optional<ProjectJpaEntity> findWithDetailsById(UUID id);
}
