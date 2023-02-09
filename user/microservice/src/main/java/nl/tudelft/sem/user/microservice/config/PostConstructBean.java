package nl.tudelft.sem.user.microservice.config;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.user.commons.entities.utils.Role;
import nl.tudelft.sem.user.microservice.database.entities.UserEntity;
import nl.tudelft.sem.user.microservice.userapi.UserEntityRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PostConstructBean {
    private final transient UserEntityRepository userRepository;

    /**
     * Instantiates a new UsersController.
     *
     * @param userRepository the registration service
     */
    @Autowired
    public PostConstructBean(UserEntityRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Initialization of ADMIN.
     *
     */
    @PostConstruct
    public void initializeAdmin() {
        try {
            UserEntity initUser = new UserEntity("ADMIN",
                    Role.ADMIN,
                    "",
                    "IT guy",
                    "Administration",
                    "User",
                    "",
                    "");
            userRepository.save(initUser);
        } catch (ConstraintViolationException e) {
            log.warn("ADMIN already exists");
        } catch (Exception e) {
            log.error("Failed to initialize ADMIN", e);
        }
    }
}
