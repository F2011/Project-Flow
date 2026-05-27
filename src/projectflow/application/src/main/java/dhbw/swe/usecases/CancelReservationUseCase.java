package dhbw.swe.usecases;

import dhbw.swe.ports.ReservationRepository;
import dhbw.swe.services.ReservationService;
import dhbw.swe.valueobjects.Reservation;
import java.util.UUID;

public class CancelReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    public CancelReservationUseCase(ReservationRepository reservationRepository,
            ReservationService reservationService) {
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
    }

    public void execute(UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "reservation not found: " + reservationId));
        reservationService.cancelReservation(reservation);
        reservationRepository.deleteById(reservationId);
    }
}
