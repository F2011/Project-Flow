package dhbw.swe.plugins.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "projects")
public class ProjectJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;

    @Column(name = "budget_amount", precision = 19, scale = 2)
    private BigDecimal budgetAmount;

    @Column(name = "budget_currency", length = 3)
    private String budgetCurrency;

    @Column(name = "duration_start")
    private LocalDateTime durationStart;

    @Column(name = "duration_end")
    private LocalDateTime durationEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyJpaEntity company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_project_id")
    private ProjectJpaEntity parentProject;

    @OneToMany(mappedBy = "parentProject", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "subproject_order")
    private List<ProjectJpaEntity> subProjects = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_required_qualifications",
            joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "qualification")
    @OrderColumn(name = "qualification_order")
    private List<String> requiredQualifications = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationJpaEntity> reservations = new ArrayList<>();

    protected ProjectJpaEntity() {}

    public ProjectJpaEntity(UUID id, String name, BigDecimal budgetAmount, String budgetCurrency,
            LocalDateTime durationStart, LocalDateTime durationEnd, CompanyJpaEntity company) {
        this.id = id;
        this.name = name;
        this.budgetAmount = budgetAmount;
        this.budgetCurrency = budgetCurrency;
        this.durationStart = durationStart;
        this.durationEnd = durationEnd;
        this.company = company;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }
    public String getBudgetCurrency() { return budgetCurrency; }
    public LocalDateTime getDurationStart() { return durationStart; }
    public LocalDateTime getDurationEnd() { return durationEnd; }
    public CompanyJpaEntity getCompany() { return company; }
    public ProjectJpaEntity getParentProject() { return parentProject; }
    public void setParentProject(ProjectJpaEntity parentProject) { this.parentProject = parentProject; }
    public List<ProjectJpaEntity> getSubProjects() { return subProjects; }
    public List<String> getRequiredQualifications() { return requiredQualifications; }
    public void setRequiredQualifications(List<String> rq) { this.requiredQualifications = rq; }
    public List<ReservationJpaEntity> getReservations() { return reservations; }
}
