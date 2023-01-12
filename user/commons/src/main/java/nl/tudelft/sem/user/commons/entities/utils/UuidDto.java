package nl.tudelft.sem.user.commons.entities.utils;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Views.Public.class)
public class UuidDto implements Dto {
    private transient UUID uuid;

    public static UuidDtoBuilder builder() {
        return new UuidDtoBuilder();
    }

    public static class UuidDtoBuilder {
        private transient UUID uuidField;

        private UuidDtoBuilder() {
        }

        public UuidDtoBuilder uuid(UUID uuid) {
            this.uuidField = uuid;
            return this;
        }

        public UuidDto build() {
            return new UuidDto(uuidField);
        }
    }
}
