package dhbw.swe.services;

import dhbw.swe.entities.Project;
import dhbw.swe.entities.Resource;
import dhbw.swe.valueobjects.Reservation;
import dhbw.swe.valueobjects.TimeRange;

public class ReservationService {

    public Reservation reserveResource(Resource resource, TimeRange timeRange, Project project) {
        if (!resource.isAvailableDuring(timeRange)) {
            throw new IllegalArgumentException(
                    "resource '" + resource.getName() + "' is not available during " + timeRange);
        }

        Reservation reservation = new Reservation(resource, timeRange, project);
        resource.addReservation(reservation);
        project.addReservation(reservation);
        return reservation;
    }

    public void cancelReservation(Reservation reservation) {
        reservation.getResource().removeReservation(reservation);
        reservation.getProject().removeReservation(reservation);
    }
}
