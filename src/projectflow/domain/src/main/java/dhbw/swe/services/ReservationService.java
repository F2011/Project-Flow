package dhbw.swe.services;

import dhbw.swe.entities.Employee;
import dhbw.swe.entities.Project;
import dhbw.swe.entities.Resource;
import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.Qualification;
import dhbw.swe.valueobjects.Reservation;
import dhbw.swe.valueobjects.TimeRange;

public class ReservationService {

    public Reservation reserveResource(Resource resource, TimeRange timeRange, Project project) {
        if (!resource.isAvailableDuring(timeRange)) {
            throw new IllegalArgumentException(
                    "resource '" + resource.getName() + "' is not available during " + timeRange);
        }

        if (resource instanceof Employee employee) {
            for (Qualification required : project.getRequiredQualifications()) {
                if (!employee.hasQualification(required)) {
                    throw new IllegalArgumentException("employee '" + employee.getName()
                            + "' lacks required qualification: " + required);
                }
            }
        }

        long hours = (long) Math.ceil(timeRange.getDuration().toMinutes() / 60.0);
        Money reservationCost = resource.getCostsPerHour().multiply((int) hours);
        Money alreadyAllocated = project.getReservations().stream().map(r -> {
            int h = (int) Math.ceil(r.getTimeRange().getDuration().toMinutes() / 60.0);
            return r.getResource().getCostsPerHour().multiply(h);
        }).reduce(reservationCost, Money::add);
        if (alreadyAllocated.compareTo(project.getBudget()) > 0) {
            throw new IllegalArgumentException("reservation cost exceeds remaining project budget");
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
