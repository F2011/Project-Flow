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
        checkAvailability(resource, timeRange);
        checkQualifications(resource, project);
        checkBudget(resource, timeRange, project);

        Reservation reservation = new Reservation(resource, timeRange, project);
        resource.addReservation(reservation);
        project.addReservation(reservation);
        return reservation;
    }

    private void checkAvailability(Resource resource, TimeRange timeRange) {
        if (!resource.isAvailableDuring(timeRange)) {
            throw new IllegalArgumentException(
                    "resource '" + resource.getName() + "' is not available during " + timeRange);
        }
    }

    private void checkQualifications(Resource resource, Project project) {
        if (resource instanceof Employee employee) {
            for (Qualification required : project.getRequiredQualifications()) {
                if (!employee.hasQualification(required)) {
                    throw new IllegalArgumentException("employee '" + employee.getName()
                            + "' lacks required qualification: " + required);
                }
            }
        }
    }

    private void checkBudget(Resource resource, TimeRange timeRange, Project project) {
        Money newCost = calculateCost(resource, timeRange);
        Money alreadyAllocated = project.getReservations().stream()
                .map(r -> calculateCost(r.getResource(), r.getTimeRange()))
                .reduce(newCost, Money::add);
        if (alreadyAllocated.compareTo(project.getBudget()) > 0) {
            throw new IllegalArgumentException("reservation cost exceeds remaining project budget");
        }
    }

    private Money calculateCost(Resource resource, TimeRange timeRange) {
        int hours = (int) Math.ceil(timeRange.getDuration().toMinutes() / 60.0);
        return resource.getCostsPerHour().multiply(hours);
    }

    public void cancelReservation(Reservation reservation) {
        reservation.getResource().removeReservation(reservation);
        reservation.getProject().removeReservation(reservation);
    }
}
