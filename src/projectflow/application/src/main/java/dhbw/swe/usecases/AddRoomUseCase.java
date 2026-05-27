package dhbw.swe.usecases;

import dhbw.swe.entities.Company;
import dhbw.swe.entities.Room;
import dhbw.swe.ports.CompanyRepository;
import dhbw.swe.ports.ResourceRepository;
import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.TimeRange;
import java.util.UUID;

public class AddRoomUseCase {

    private final CompanyRepository companyRepository;
    private final ResourceRepository resourceRepository;

    public AddRoomUseCase(CompanyRepository companyRepository,
            ResourceRepository resourceRepository) {
        this.companyRepository = companyRepository;
        this.resourceRepository = resourceRepository;
    }

    public Room execute(UUID companyId, String name, int size, Money costsPerHour,
            String roomNumber, TimeRange availability) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("company not found: " + companyId));
        Room room = new Room(UUID.randomUUID(), name, size, costsPerHour, roomNumber, availability);
        company.addResource(room);
        resourceRepository.save(room);
        companyRepository.save(company);
        return room;
    }
}
