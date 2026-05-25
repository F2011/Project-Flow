package dhbw.swe.ports;

import dhbw.swe.entities.Company;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    void save(Company company);
    Optional<Company> findById(UUID id);
}
