package nl.tudelft.sem.user.microservice.config;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.template.authentication.domain.user.AppUser;
import nl.tudelft.sem.template.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.template.authentication.domain.user.NetId;
import nl.tudelft.sem.template.authentication.domain.user.UserRepository;
import nl.tudelft.sem.user.commons.entities.utils.Role;
import nl.tudelft.sem.user.microservice.database.entities.utils.UserEntity;
import nl.tudelft.sem.user.microservice.userapi.UserEntityRepository;
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
     * @throws Exception in case registration fails
     */
    @PostConstruct
    public void initializeAdmin() throws Exception {

        try {
            UserEntity initUser = new UserEntity("ADMIN", Role.ADMIN, "", "IT guy",
                    "Administration", "User", "", "");
            userRepository.save(initUser);
        } catch (Exception e) {
            log.error("ADMIN already exists", e);
        }
    }
}
