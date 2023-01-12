package nl.tudelft.sem.user.microservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nl.tudelft.sem.user.commons.entities.utils.Role;
import nl.tudelft.sem.user.commons.entities.utils.UserDto;
import nl.tudelft.sem.user.commons.entities.utils.UserModify;
import nl.tudelft.sem.user.microservice.database.entities.utils.UserEntity;
import nl.tudelft.sem.user.microservice.exceptions.UserNotFoundException;
import nl.tudelft.sem.user.microservice.service.UserService;
import nl.tudelft.sem.user.microservice.userapi.UserEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserEntityRepository userEntityRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void addUserByUserEntityTest() {

        userEntityRepository = mock(UserEntityRepository.class);
        userService = new UserService(userEntityRepository);

        UserEntity user1 = new UserEntity("SomeNetId", Role.CANDIDATE, "someAddress", "SomeDescription", "SomeFirstName",
                "SomeLastName", "SomeEmail", "SomePhoneNumber");
        when(userEntityRepository
                .save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        userService.addUser(user1);
        verify(userEntityRepository, times(1))
                .save(any());

    }

    @Test
    void addUserByNetIdTest() {

        userEntityRepository = mock(UserEntityRepository.class);
        userService = new UserService(userEntityRepository);

        String netId = "SomeNetId";
        when(userEntityRepository
                .save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        userService.addUser(netId);
        verify(userEntityRepository, times(1))
                .save(any());

    }

    @Test
    void updateUserTest() {
        UserModify userDto = UserModify.builder()
                        .address("newAddress")
                        .description("newDescription")
                        .role(Role.FIRED)
                        .netId("SomeNetId")
                        .email("newEmail")
                        .firstName("newFirstName")
                        .lastName("newLastName")
                        .phoneNumber("newPhoneNumber")
                        .build();

        userEntityRepository = mock(UserEntityRepository.class);
        userService = new UserService(userEntityRepository);
        UserEntity user1 = new UserEntity("SomeNetId", Role.CANDIDATE, "someAddress", "SomeDescription", "SomeFirstName",
                "SomeLastName", "SomeEmail", "SomePhoneNumber");

        when(userEntityRepository
                .findByNetId(userDto.getNetId()))
                .thenReturn(Optional.of(user1));

        when(userEntityRepository
                .save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));



        UserEntity updatedUser = userService.updateUser(userDto);

        // verify(userEntityRepository).deleteById(any());
        verify(userEntityRepository, times(1)).save(any());

        assertEquals(updatedUser.getId(), user1.getId());
        assertEquals(updatedUser.getAddress(), userDto.getAddress());
        assertEquals(updatedUser.getDescription(), userDto.getDescription());
        assertEquals(updatedUser.getNetId(), userDto.getNetId());
        assertEquals(updatedUser.getRole(), userDto.getRole());
        assertEquals(updatedUser.getEmail(), userDto.getEmail());
        assertEquals(updatedUser.getPhoneNumber(), userDto.getPhoneNumber());
        assertEquals(updatedUser.getFirstName(), userDto.getFirstName());
        assertEquals(updatedUser.getLastName(), userDto.getLastName());
    }

    @Test
    void updateUserNotFoundTest() {
        UserModify userDto = new UserModify();
        userDto.setAddress("newAddress");
        userDto.setDescription("newDescription");
        userDto.setRole(Role.FIRED);
        userDto.setNetId("FakeUser");
        userDto.setEmail("newEmail");
        userDto.setFirstName("newFirstName");
        userDto.setLastName("newLastName");
        userDto.setPhoneNumber("newPhoneNumber");

        userEntityRepository = mock(UserEntityRepository.class);
        userService = new UserService(userEntityRepository);
        UserEntity user1 = new UserEntity("SomeNetId", Role.CANDIDATE, "someAddress", "SomeDescription", "SomeFirstName",
                "SomeLastName", "SomeEmail", "SomePhoneNumber");

        when(userEntityRepository
                .findByNetId(userDto.getNetId()))
                .thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userDto));

    }

    @Test
    void updateUserPartiallyTest() {
        UserModify userDto = new UserModify();
        userDto.setAddress("newAddress");
        userDto.setNetId("SomeNetId");
        // Description is Null
        // Role is Null
        // Address is Null
        // Email is Null
        // First, Last name are Null
        // => Won't be updated

        userEntityRepository = mock(UserEntityRepository.class);
        userService = new UserService(userEntityRepository);
        UserEntity user1 = new UserEntity("SomeNetId", Role.CANDIDATE, "someAddress", "SomeDescription", "SomeFirstName",
                "SomeLastName", "SomeEmail", "SomePhoneNumber");

        when(userEntityRepository
                .save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(userEntityRepository
                .findByNetId(userDto.getNetId()))
                .thenReturn(Optional.of(user1));

        UserEntity updatedUser = userService.updateUser(userDto);

        // verify(userEntityRepository).deleteById(any());
        verify(userEntityRepository, times(1)).save(any());

        assertEquals(updatedUser.getId(), user1.getId());
        assertEquals(updatedUser.getAddress(), userDto.getAddress());
        assertEquals(updatedUser.getDescription(), user1.getDescription());
        assertEquals(updatedUser.getRole(), user1.getRole());
        assertEquals(updatedUser.getNetId(), userDto.getNetId());
        assertEquals(updatedUser.getFirstName(), user1.getFirstName());
        assertEquals(updatedUser.getLastName(), user1.getLastName());
        assertEquals(updatedUser.getEmail(), user1.getEmail());
        assertEquals(updatedUser.getPhoneNumber(), user1.getPhoneNumber());
    }
}
