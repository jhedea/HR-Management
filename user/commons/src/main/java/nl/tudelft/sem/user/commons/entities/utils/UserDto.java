package nl.tudelft.sem.user.commons.entities.utils;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Data transfer object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Views.Public.class)
public class UserDto implements Dto {
    private UUID id;
    private String netId;
    private Role role;
    private String address;
    private String description;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public static class UserDtoBuilder {
        private transient UUID id;
        private transient String netIdField;
        private transient Role roleField;
        private transient String addressField;
        private transient String descriptionField;
        private transient String firstNameField;
        private transient String lastNameField;
        private transient String emailField;
        private transient String phoneNumberField;

        private UserDtoBuilder() {
        }

        public UserDtoBuilder uuid(UUID id) {
            this.id = id;
            return this;
        }

        public UserDtoBuilder netId(String netId) {
            this.netIdField = netId;
            return this;
        }

        public UserDtoBuilder role(Role role) {
            this.roleField = role;
            return this;
        }

        public UserDtoBuilder address(String address) {
            this.addressField = address;
            return this;
        }

        public UserDtoBuilder description(String description) {
            this.descriptionField = description;
            return this;
        }

        public UserDtoBuilder firstName(String firstName) {
            this.firstNameField = firstName;
            return this;
        }

        public UserDtoBuilder lastName(String lastName) {
            this.lastNameField = lastName;
            return this;
        }

        public UserDtoBuilder email(String email) {
            this.emailField = email;
            return this;
        }

        public UserDtoBuilder phoneNumber(String phoneNumber) {
            this.phoneNumberField = phoneNumber;
            return this;
        }

        /**
         * Build the DTO.
         *
         * @return Generated DTO.
         */
        public UserDto build() {
            return new UserDto(id,
                    netIdField,
                    roleField,
                    addressField,
                    descriptionField,
                    firstNameField,
                    lastNameField,
                    emailField,
                    phoneNumberField);
        }
    }
}
