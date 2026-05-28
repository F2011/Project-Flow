package dhbw.swe.plugins.config;

import dhbw.swe.plugins.persistence.*;
import dhbw.swe.services.ReservationService;
import dhbw.swe.usecases.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ReservationService reservationService() {
        return new ReservationService();
    }

    @Bean
    public HireEmployeeUseCase hireEmployeeUseCase(
            CompanyRepositoryAdapter companyRepository,
            ResourceRepositoryAdapter resourceRepository) {
        return new HireEmployeeUseCase(companyRepository, resourceRepository);
    }

    @Bean
    public AddQualificationUseCase addQualificationUseCase(
            ResourceRepositoryAdapter resourceRepository) {
        return new AddQualificationUseCase(resourceRepository);
    }

    @Bean
    public AddRoomUseCase addRoomUseCase(
            CompanyRepositoryAdapter companyRepository,
            ResourceRepositoryAdapter resourceRepository) {
        return new AddRoomUseCase(companyRepository, resourceRepository);
    }

    @Bean
    public CreateProjectUseCase createProjectUseCase(
            CompanyRepositoryAdapter companyRepository,
            ProjectRepositoryAdapter projectRepository) {
        return new CreateProjectUseCase(companyRepository, projectRepository);
    }

    @Bean
    public CreateSubProjectUseCase createSubProjectUseCase(
            ProjectRepositoryAdapter projectRepository) {
        return new CreateSubProjectUseCase(projectRepository);
    }

    @Bean
    public BookResourceUseCase bookResourceUseCase(
            ProjectRepositoryAdapter projectRepository,
            ResourceRepositoryAdapter resourceRepository,
            ReservationRepositoryAdapter reservationRepository,
            ReservationService reservationService) {
        return new BookResourceUseCase(projectRepository, resourceRepository,
                reservationRepository, reservationService);
    }

    @Bean
    public CancelReservationUseCase cancelReservationUseCase(
            ReservationRepositoryAdapter reservationRepository,
            ResourceRepositoryAdapter resourceRepository,
            ProjectRepositoryAdapter projectRepository,
            ReservationService reservationService) {
        return new CancelReservationUseCase(reservationRepository, resourceRepository,
                projectRepository, reservationService);
    }

    @Bean
    public GenerateReportUseCase generateReportUseCase(
            ResourceRepositoryAdapter resourceRepository) {
        return new GenerateReportUseCase(resourceRepository);
    }
}
