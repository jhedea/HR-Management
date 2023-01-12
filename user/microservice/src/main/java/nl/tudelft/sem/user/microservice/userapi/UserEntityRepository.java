package nl.tudelft.sem.user.microservice.userapi;

import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import nl.tudelft.sem.user.microservice.database.entities.utils.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserEntityRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByNetId(@NonNull String netId);

    Optional<UserEntity> findById(UUID fromString);
}
