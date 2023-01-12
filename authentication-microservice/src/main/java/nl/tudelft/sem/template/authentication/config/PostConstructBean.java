package nl.tudelft.sem.template.authentication.config;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.template.authentication.domain.providers.implementations.ClientProvider;
import nl.tudelft.sem.template.authentication.domain.user.AppUser;
import nl.tudelft.sem.template.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.template.authentication.domain.user.NetId;
import nl.tudelft.sem.template.authentication.domain.user.Password;
import nl.tudelft.sem.template.authentication.domain.user.PasswordHashingService;
import nl.tudelft.sem.template.authentication.domain.user.UserRepository;
import nl.tudelft.sem.user.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PostConstructBean {

    private final transient UserRepository userRepository;

    /**
     * Instantiates a new UsersController.
     *
     * @param userRepository   the registration service
     */
    @Autowired
    public PostConstructBean(UserRepository userRepository) {

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
            // this Hashed Password can be decoded to password: 0000
            userRepository.save(new AppUser(new NetId("ADMIN"),
                    new HashedPassword("$2a$10$h4jkAjdm4rsJ74ZHXIWJt.MKcDCBapgS82vwunfZdxKx5qX9L2hQu")));
        } catch (Exception e) {
            log.error("ADMIN already exists", e);
        }
    }
}
