package dhbw.swe.usecases;

import dhbw.swe.ports.ProjectRepository;
import dhbw.swe.ports.ReservationRepository;
import dhbw.swe.ports.ResourceRepository;
import dhbw.swe.services.ReservationService;
import dhbw.swe.valueobjects.Reservation;
import java.util.UUID;

public class CancelReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;
    private final ReservationService reservationService;

    public CancelReservationUseCase(ReservationRepository reservationRepository,
            ResourceRepository resourceRepository,
            ProjectRepository projectRepository,
            ReservationService reservationService) {
        this.reservationRepository = reservationRepository;
        this.resourceRepository = resourceRepository;
        this.projectRepository = projectRepository;
        this.reservationService = reservationService;
    }

    public void execute(UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "reservation not found: " + reservationId));
        reservationService.cancelReservation(reservation);
        resourceRepository.save(reservation.getResource());
        projectRepository.save(reservation.getProject());
        reservationRepository.deleteById(reservationId);
    }
}
