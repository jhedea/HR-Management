package nl.tudelft.sem.contract.microservice.database.repositories;

import java.util.UUID;
import nl.tudelft.sem.contract.microservice.database.entities.SalaryScale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryScaleRepository extends JpaRepository<SalaryScale, UUID> {
}
