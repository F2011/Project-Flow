package dhbw.swe.plugins.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@DiscriminatorValue("ROOM")
public class RoomJpaEntity extends ResourceJpaEntity {

    @Column(name = "room_size")
    private Integer size;

    @Column(name = "room_number")
    private String roomNumber;

    protected RoomJpaEntity() {}

    public RoomJpaEntity(UUID id, String name, LocalDateTime availabilityStart,
            LocalDateTime availabilityEnd, BigDecimal costsPerHourAmount,
            String costsPerHourCurrency, CompanyJpaEntity company, int size, String roomNumber) {
        super(id, name, availabilityStart, availabilityEnd, costsPerHourAmount,
                costsPerHourCurrency, company);
        this.size = size;
        this.roomNumber = roomNumber;
    }

    public Integer getSize() { return size; }
    public String getRoomNumber() { return roomNumber; }
}
