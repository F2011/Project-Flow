package dhbw.swe.entities;

import java.util.UUID;

import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.TimeRange;

public class Room extends Resource {

    private final int size; // in persons
    private final String roomNumber;

    private Money costsPerHour;

    public Room(UUID id, String name, int size, Money costsPerHour, String roomNumber,
            TimeRange availability) {
        super(id, name, availability);
        this.size = size;
        this.costsPerHour = costsPerHour;
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public boolean isBigEnoughFor(int amountEmployees) {
        return this.size >= amountEmployees;
    }

    public Money getCostsPerHour() {
        return this.costsPerHour;
    }
}
