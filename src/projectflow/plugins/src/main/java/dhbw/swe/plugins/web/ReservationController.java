package dhbw.swe.plugins.web;

import dhbw.swe.usecases.BookResourceUseCase;
import dhbw.swe.usecases.CancelReservationUseCase;
import dhbw.swe.usecases.GenerateReportUseCase;
import dhbw.swe.valueobjects.TimeRange;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Reservations")
public class ReservationController {

    private final BookResourceUseCase bookResourceUseCase;
    private final CancelReservationUseCase cancelReservationUseCase;
    private final GenerateReportUseCase generateReportUseCase;

    public ReservationController(BookResourceUseCase bookResourceUseCase,
            CancelReservationUseCase cancelReservationUseCase,
            GenerateReportUseCase generateReportUseCase) {
        this.bookResourceUseCase = bookResourceUseCase;
        this.cancelReservationUseCase = cancelReservationUseCase;
        this.generateReportUseCase = generateReportUseCase;
    }

    public record BookResourceRequest(UUID resourceId, LocalDateTime timeStart,
            LocalDateTime timeEnd) {
    }

    public record ReservationResponse(UUID resourceId, String resourceName, UUID projectId,
            LocalDateTime timeStart, LocalDateTime timeEnd) {
    }

    @PostMapping("/projects/{projectId}/reservations")
    @Operation(summary = "Book a resource for a project")
    public ResponseEntity<Void> book(@PathVariable UUID projectId,
            @RequestBody BookResourceRequest req) {
        TimeRange timeRange = new TimeRange(req.timeStart(), req.timeEnd());
        UUID reservationId = bookResourceUseCase.execute(projectId, req.resourceId(), timeRange);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/reservations/{id}").buildAndExpand(reservationId).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/reservations/{id}")
    @Operation(summary = "Cancel a reservation")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) {
        cancelReservationUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resources/{resourceId}/report")
    @Operation(summary = "Generate a reservation report for a resource within a time range")
    public ResponseEntity<List<ReservationResponse>> report(@PathVariable UUID resourceId,
            @RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
        TimeRange timeRange = new TimeRange(from, to);
        List<ReservationResponse> result =
                generateReportUseCase.execute(resourceId, timeRange).stream()
                        .map(r -> new ReservationResponse(r.getResource().getId(),
                                r.getResource().getName(), r.getProject().getId(),
                                r.getTimeRange().getStart(), r.getTimeRange().getEnd()))
                        .toList();
        return ResponseEntity.ok(result);
    }
}
