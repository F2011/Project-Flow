package dhbw.swe.plugins.persistence.spring;

import dhbw.swe.plugins.persistence.entity.ResourceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ResourceJpaRepository extends JpaRepository<ResourceJpaEntity, UUID> {}
