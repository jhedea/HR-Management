package nl.tudelft.sem.notification.microservice.database.repositories;

import java.util.UUID;
import nl.tudelft.sem.notification.microservice.database.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageEntityRepository extends JpaRepository<MessageEntity, UUID> {
}