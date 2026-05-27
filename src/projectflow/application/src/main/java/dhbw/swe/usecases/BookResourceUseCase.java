package dhbw.swe.usecases;

import dhbw.swe.entities.Project;
import dhbw.swe.entities.Resource;
import dhbw.swe.ports.ProjectRepository;
import dhbw.swe.ports.ReservationRepository;
import dhbw.swe.ports.ResourceRepository;
import dhbw.swe.services.ReservationService;
import dhbw.swe.valueobjects.Reservation;
import dhbw.swe.valueobjects.TimeRange;
import java.util.UUID;

public class BookResourceUseCase {

    private final ProjectRepository projectRepository;
    private final ResourceRepository resourceRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    public BookResourceUseCase(ProjectRepository projectRepository,
            ResourceRepository resourceRepository,
            ReservationRepository reservationRepository,
            ReservationService reservationService) {
        this.projectRepository = projectRepository;
        this.resourceRepository = resourceRepository;
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
    }

    public UUID execute(UUID projectId, UUID resourceId, TimeRange timeRange) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("project not found: " + projectId));
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(
                        () -> new IllegalArgumentException("resource not found: " + resourceId));

        if (!project.getDuration().includes(timeRange)) {
            throw new IllegalArgumentException(
                    "reservation must be within project duration");
        }

        Reservation reservation = reservationService.reserveResource(resource, timeRange, project);
        UUID reservationId = UUID.randomUUID();
        reservationRepository.save(reservationId, reservation);
        projectRepository.save(project);
        resourceRepository.save(resource);
        return reservationId;
    }
}
