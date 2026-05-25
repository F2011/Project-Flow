package dhbw.swe.ports;

import dhbw.swe.valueobjects.Reservation;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {
    void save(UUID id, Reservation reservation);
    Optional<Reservation> findById(UUID id);
    void deleteById(UUID id);
}
