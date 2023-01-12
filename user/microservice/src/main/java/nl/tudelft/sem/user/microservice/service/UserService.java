package nl.tudelft.sem.user.microservice.service;

import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import nl.tudelft.sem.user.commons.entities.utils.Role;
import nl.tudelft.sem.user.commons.entities.utils.UserDto;
import nl.tudelft.sem.user.commons.entities.utils.UserModify;
import nl.tudelft.sem.user.microservice.database.entities.utils.UserEntity;
import nl.tudelft.sem.user.microservice.exceptions.UserNotFoundException;
import nl.tudelft.sem.user.microservice.userapi.UserEntityRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final transient UserEntityRepository userEntityRepository;

    public UserService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    public void addUser(String netId) {
        userEntityRepository.save(new UserEntity(netId, Role.EMPLOYEE, "", "", "", "", "", ""));
    }

    /**
     * Adds user to the repository.
     *
     * @param user that we want to store
     */
    public void addUser(UserEntity user) {
        userEntityRepository.save(user);
    }

    /**
     * Updates the User in the repository with new information about him.
     *
     * @param userDto New information that we want to pass on
     */
    public UserEntity updateUser(@Valid UserModify userDto) {

        var user = userEntityRepository
                .findByNetId(userDto.getNetId())
                .orElseThrow(UserNotFoundException::new);
        UUID id = user.getId();
        // delete the already existing instance of User to create the updated one
        //userEntityRepository.deleteById(id.toString());
        UserEntity updatedUser = new UserEntity();
        // give the same id as before for mapping purposes
        updatedUser.setId(id);
        updatedUser.setNetId(userDto.getNetId());


        if (userDto.getAddress() != null) {
            updatedUser.setAddress(userDto.getAddress());
        } else {
            updatedUser.setAddress(user.getAddress());
        }
        if (userDto.getDescription() != null) {
            updatedUser.setDescription(userDto.getDescription());
        } else {
            updatedUser.setDescription(user.getDescription());
        }
        if (userDto.getRole() != null) {
            updatedUser.setRole(userDto.getRole());
        } else {
            updatedUser.setRole(user.getRole());
        }
        if (userDto.getFirstName() != null) {
            updatedUser.setFirstName(userDto.getFirstName());
        } else {
            updatedUser.setFirstName(user.getFirstName());
        }
        if (userDto.getLastName() != null) {
            updatedUser.setLastName(userDto.getLastName());
        } else {
            updatedUser.setLastName(user.getLastName());
        }
        if (userDto.getEmail() != null) {
            updatedUser.setEmail(userDto.getEmail());
        } else {
            updatedUser.setEmail(user.getEmail());
        }
        if (userDto.getPhoneNumber() != null) {
            updatedUser.setPhoneNumber(userDto.getPhoneNumber());
        } else {
            updatedUser.setPhoneNumber(user.getPhoneNumber());
        }
        // NetId cannot be updated
        try {
            userEntityRepository.save(updatedUser);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("SAVED SUCCESSFUL");
        return updatedUser;
    }


}
