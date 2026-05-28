package dhbw.swe.plugins.persistence;

import dhbw.swe.entities.Company;
import dhbw.swe.plugins.persistence.entity.CompanyJpaEntity;
import dhbw.swe.plugins.persistence.spring.CompanyJpaRepository;
import dhbw.swe.ports.CompanyRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CompanyRepositoryAdapter implements CompanyRepository {

    private final CompanyJpaRepository jpa;

    public CompanyRepositoryAdapter(CompanyJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(Company company) {
        CompanyJpaEntity existing = jpa.findById(company.getId()).orElse(null);
        CompanyJpaEntity entity = DomainMapper.toJpaCompany(company, existing);
        jpa.save(entity);
    }

    @Override
    public Optional<Company> findById(UUID id) {
        return jpa.findById(id).map(DomainMapper::toDomainCompany);
    }
}
