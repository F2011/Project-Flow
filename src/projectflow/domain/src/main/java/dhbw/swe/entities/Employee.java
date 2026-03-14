package dhbw.swe.entities;

import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.Qualification;
import dhbw.swe.valueobjects.TimeRange;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class Employee extends Resource {

    private final Set<Qualification> qualifications;

    private Money costsPerHour;


    public Employee(UUID id, String name, Money costsPerHour, TimeRange availability,
            Set<Qualification> qualifications) {
        super(id, name, availability);
        this.costsPerHour = costsPerHour;
        this.qualifications = EnumSet.copyOf(qualifications);
    }

    public Employee(UUID id, String name, Money costsPerHour, TimeRange availability) {
        this(id, name, costsPerHour, availability, EnumSet.noneOf(Qualification.class));
    }

    public Money getCostsPerHour() {
        return costsPerHour;
    }

    public Set<Qualification> getQualifications() {
        return qualifications;
    }

    public void addQualification(Qualification qualification) {
        qualifications.add(qualification);
    }

    public void removeQualification(Qualification qualification) {
        qualifications.remove(qualification);
    }

    public boolean hasQualification(Qualification qualification) {
        return qualifications.contains(qualification);
    }
}
