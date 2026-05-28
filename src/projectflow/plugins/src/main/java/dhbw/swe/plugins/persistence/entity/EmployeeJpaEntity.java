package dhbw.swe.plugins.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@DiscriminatorValue("EMPLOYEE")
public class EmployeeJpaEntity extends ResourceJpaEntity {

    @ElementCollection
    @CollectionTable(name = "employee_qualifications", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "qualification")
    private List<String> qualifications = new ArrayList<>();

    protected EmployeeJpaEntity() {}

    public EmployeeJpaEntity(UUID id, String name, LocalDateTime availabilityStart,
            LocalDateTime availabilityEnd, BigDecimal costsPerHourAmount,
            String costsPerHourCurrency, CompanyJpaEntity company) {
        super(id, name, availabilityStart, availabilityEnd, costsPerHourAmount,
                costsPerHourCurrency, company);
    }

    public List<String> getQualifications() { return qualifications; }
    public void setQualifications(List<String> qualifications) { this.qualifications = qualifications; }
}
