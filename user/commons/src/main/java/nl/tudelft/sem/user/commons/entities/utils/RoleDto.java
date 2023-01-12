package nl.tudelft.sem.user.commons.entities.utils;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Views.Public.class)
public class RoleDto implements Dto {

    private transient Role role;

    public static RoleDtoBuilder builder() {
        return new RoleDtoBuilder();
    }

    public static class RoleDtoBuilder {
        private transient Role userRole;

        private RoleDtoBuilder() {
        }

        public RoleDtoBuilder role(Role role) {
            this.userRole = role;
            return this;
        }

        public RoleDto build() {
            return new RoleDto(userRole);
        }

    }
}
