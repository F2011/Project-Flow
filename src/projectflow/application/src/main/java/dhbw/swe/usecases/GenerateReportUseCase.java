package dhbw.swe.usecases;

import dhbw.swe.entities.Resource;
import dhbw.swe.ports.ResourceRepository;
import dhbw.swe.valueobjects.Reservation;
import dhbw.swe.valueobjects.TimeRange;
import java.util.List;
import java.util.UUID;

public class GenerateReportUseCase {

    private final ResourceRepository resourceRepository;

    public GenerateReportUseCase(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Reservation> execute(UUID resourceId, TimeRange timeRange) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(
                        () -> new IllegalArgumentException("resource not found: " + resourceId));
        return resource.getReservations().stream()
                .filter(r -> r.getTimeRange().overlaps(timeRange))
                .toList();
    }
}
