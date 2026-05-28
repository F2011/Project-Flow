package dhbw.swe.plugins.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "resources")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class ResourceJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;

    @Column(name = "availability_start")
    private LocalDateTime availabilityStart;

    @Column(name = "availability_end")
    private LocalDateTime availabilityEnd;

    @Column(name = "costs_per_hour_amount", precision = 19, scale = 2)
    private BigDecimal costsPerHourAmount;

    @Column(name = "costs_per_hour_currency", length = 3)
    private String costsPerHourCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyJpaEntity company;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationJpaEntity> reservations = new ArrayList<>();

    protected ResourceJpaEntity() {}

    protected ResourceJpaEntity(UUID id, String name, LocalDateTime availabilityStart,
            LocalDateTime availabilityEnd, BigDecimal costsPerHourAmount,
            String costsPerHourCurrency, CompanyJpaEntity company) {
        this.id = id;
        this.name = name;
        this.availabilityStart = availabilityStart;
        this.availabilityEnd = availabilityEnd;
        this.costsPerHourAmount = costsPerHourAmount;
        this.costsPerHourCurrency = costsPerHourCurrency;
        this.company = company;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public LocalDateTime getAvailabilityStart() { return availabilityStart; }
    public LocalDateTime getAvailabilityEnd() { return availabilityEnd; }
    public BigDecimal getCostsPerHourAmount() { return costsPerHourAmount; }
    public String getCostsPerHourCurrency() { return costsPerHourCurrency; }
    public CompanyJpaEntity getCompany() { return company; }
    public void setCompany(CompanyJpaEntity company) { this.company = company; }
    public List<ReservationJpaEntity> getReservations() { return reservations; }
}
