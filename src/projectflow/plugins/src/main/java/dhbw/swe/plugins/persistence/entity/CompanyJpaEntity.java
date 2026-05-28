package dhbw.swe.plugins.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "companies")
public class CompanyJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;

    @Column(name = "budget_amount", precision = 19, scale = 2)
    private BigDecimal budgetAmount;

    @Column(name = "budget_currency", length = 3)
    private String budgetCurrency;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectJpaEntity> projects = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResourceJpaEntity> resources = new ArrayList<>();

    protected CompanyJpaEntity() {}

    public CompanyJpaEntity(UUID id, String name, BigDecimal budgetAmount, String budgetCurrency) {
        this.id = id;
        this.name = name;
        this.budgetAmount = budgetAmount;
        this.budgetCurrency = budgetCurrency;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }
    public String getBudgetCurrency() { return budgetCurrency; }
    public void setBudgetCurrency(String budgetCurrency) { this.budgetCurrency = budgetCurrency; }
    public List<ProjectJpaEntity> getProjects() { return projects; }
    public List<ResourceJpaEntity> getResources() { return resources; }
}
