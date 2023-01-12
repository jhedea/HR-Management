package nl.tudelft.sem.request.microservice.database.repositories;

import java.util.UUID;
import nl.tudelft.sem.request.microservice.database.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RequestRepository extends JpaRepository<Request, UUID>, JpaSpecificationExecutor<Request> {
}