package nl.tudelft.sem.user.commons.entities.utils;

import lombok.Getter;

public enum Role {

    FIRED(4),
    CANDIDATE(3),
    EMPLOYEE(2),
    HR(1),
    ADMIN(0);

    @Getter
    private final int value;

    Role(int value) {
        this.value = value;
    }

    /**
     * Get a DTO of the role.
     *
     * @return the role as a DTO.
     */
    public RoleDto getDto() {
        return new RoleDto(this);
    }
}