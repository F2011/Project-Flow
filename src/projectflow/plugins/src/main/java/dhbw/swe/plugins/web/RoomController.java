package dhbw.swe.plugins.web;

import dhbw.swe.entities.Room;
import dhbw.swe.usecases.AddRoomUseCase;
import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.TimeRange;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@RestController
@Tag(name = "Rooms")
public class RoomController {

    private final AddRoomUseCase addRoomUseCase;

    public RoomController(AddRoomUseCase addRoomUseCase) {
        this.addRoomUseCase = addRoomUseCase;
    }

    public record AddRoomRequest(String name, int size, BigDecimal costsPerHourAmount,
            String costsPerHourCurrency, String roomNumber, LocalDateTime availabilityStart,
            LocalDateTime availabilityEnd) {}

    public record RoomResponse(UUID id, String name, int size, String roomNumber,
            BigDecimal costsPerHourAmount, String costsPerHourCurrency) {}

    @PostMapping("/companies/{companyId}/rooms")
    @Operation(summary = "Add a room to a company")
    public ResponseEntity<RoomResponse> addRoom(@PathVariable UUID companyId,
            @RequestBody AddRoomRequest req) {
        Money costs = new Money(req.costsPerHourAmount(), Currency.getInstance(req.costsPerHourCurrency()));
        TimeRange availability = new TimeRange(req.availabilityStart(), req.availabilityEnd());
        Room room = addRoomUseCase.execute(companyId, req.name(), req.size(), costs,
                req.roomNumber(), availability);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/rooms/{id}").buildAndExpand(room.getId()).toUri();
        return ResponseEntity.created(location).body(toResponse(room));
    }

    private RoomResponse toResponse(Room r) {
        return new RoomResponse(r.getId(), r.getName(), r.getSize(), r.getRoomNumber(),
                r.getCostsPerHour().getAmount(),
                r.getCostsPerHour().getCurrency().getCurrencyCode());
    }
}
