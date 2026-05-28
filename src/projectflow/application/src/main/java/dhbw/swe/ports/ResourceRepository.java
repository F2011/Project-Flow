package dhbw.swe.ports;

import dhbw.swe.entities.Employee;
import dhbw.swe.entities.Resource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResourceRepository {
    void save(Resource resource);
    Optional<Resource> findById(UUID id);
    List<Employee> findAllEmployees();
}
