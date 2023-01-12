package nl.tudelft.sem.contract.commons.entities.utils;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Views.Public.class)
public class StringDto implements Dto {
    protected transient String data;

    public static StringDtoBuilder builder() {
        return new StringDtoBuilder();
    }

    public static class StringDtoBuilder {
        private transient String dataForm;

        StringDtoBuilder() {
        }

        public StringDtoBuilder data(String data) {
            this.dataForm = data;
            return this;
        }

        public StringDto build() {
            return new StringDto(dataForm);
        }
    }
}
