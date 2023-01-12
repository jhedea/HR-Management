package nl.tudelft.sem.user.commons.entities.utils;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Data transfer object for updating user information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Views.Public.class)
public class UserModify implements Dto {
    private String netId;
    private Role role;
    private String address;
    private String description;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public static UserModifyBuilder builder() {
        return new UserModifyBuilder();
    }

    public static class UserModifyBuilder {
        private transient String netId1;
        private transient Role role1;
        private transient String address1;
        private transient String description1;
        private transient String firstName1;
        private transient String lastName1;
        private transient String email1;
        private transient String phoneNumber1;

        UserModifyBuilder() {
        }

        public UserModifyBuilder netId(String netId) {
            this.netId1 = netId;
            return this;
        }

        public UserModifyBuilder role(Role role) {
            this.role1 = role;
            return this;
        }

        public UserModifyBuilder address(String address) {
            this.address1 = address;
            return this;
        }

        public UserModifyBuilder description(String description) {
            this.description1 = description;
            return this;
        }

        public UserModifyBuilder firstName(String firstName) {
            this.firstName1 = firstName;
            return this;
        }

        public UserModifyBuilder lastName(String lastName) {
            this.lastName1 = lastName;
            return this;
        }

        public UserModifyBuilder email(String email) {
            this.email1 = email;
            return this;
        }

        public UserModifyBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber1 = phoneNumber;
            return this;
        }

        public UserModify build() {
            return new UserModify(netId1, role1, address1, description1, firstName1, lastName1, email1, phoneNumber1);
        }

        /**
         * To string method for User DTO.
         *
         * @return a string representation of the DTO.
         */
        public String toString() {
            return "UserModify.UserModifyBuilder(netId=" + this.netId1 + ", role=" + this.role1 + ", address="
                    + this.address1 + ", description=" + this.description1 + ", firstName=" + this.firstName1 + ", lastName="
                    + this.lastName1 + ", email=" + this.email1 + ", phoneNumber=" + this.phoneNumber1 + ")";
        }
    }
}
