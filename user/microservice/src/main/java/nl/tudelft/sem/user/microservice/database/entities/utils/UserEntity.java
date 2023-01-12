package nl.tudelft.sem.user.microservice.database.entities.utils;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.user.commons.entities.utils.Role;
import nl.tudelft.sem.user.commons.entities.utils.UserDto;
import nl.tudelft.sem.user.microservice.authentication.AuthManager;
import nl.tudelft.sem.user.microservice.controllers.DefaultController;
import nl.tudelft.sem.user.microservice.exceptions.UserAlreadyExists;
import nl.tudelft.sem.user.microservice.exceptions.UserNetIdCannotBeEmpty;
import nl.tudelft.sem.user.microservice.exceptions.UserNetIdTooLarge;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
public class UserEntity extends BaseEntity<UserDto> {
    @Column(nullable = false, unique = true)
    private String netId;
    @Enumerated
    @Column(nullable = false)
    private Role role;
    @Column(nullable = false)
    private String address;
    private String description;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        return Objects.equals(this.getId(),
                that.getId())
                &&
                Objects.equals(address, that.address);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRole(), getAddress(), getDescription(), getNetId());
    }

    @Override
    public UserDto getDto() {
        return new ModelMapper().map(this, UserDto.class);
    }

    public UUID getUuid() {
        return super.getId();
    }
}
