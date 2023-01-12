package nl.tudelft.sem.contract.microservice.database.repositories;

import java.util.UUID;
import nl.tudelft.sem.contract.microservice.database.entities.PensionScheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PensionSchemeRepository extends JpaRepository<PensionScheme, UUID> {
}
