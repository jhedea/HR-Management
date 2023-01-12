package nl.tudelft.sem.contract.microservice.database.repositories;

import java.util.Optional;
import java.util.UUID;
import nl.tudelft.sem.contract.microservice.database.entities.JobPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPositionRepository extends JpaRepository<JobPosition, UUID> {
    Optional<JobPosition> findByName(String name);
}
