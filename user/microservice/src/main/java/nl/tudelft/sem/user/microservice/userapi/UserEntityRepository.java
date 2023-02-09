package nl.tudelft.sem.user.microservice.userapi;

import java.util.Optional;
import java.util.UUID;
import nl.tudelft.sem.user.microservice.database.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserEntityRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByNetId(@NonNull String netId);

    Optional<UserEntity> findById(UUID fromString);
}
