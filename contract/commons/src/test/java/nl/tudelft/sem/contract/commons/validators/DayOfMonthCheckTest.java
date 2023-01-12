package nl.tudelft.sem.contract.commons.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import javax.validation.Payload;
import org.junit.jupiter.api.Test;

class DayOfMonthCheckTest {
    @Test
    void testIsValid() {
        DayOfMonthCheck dayOfMonthCheck = new DayOfMonthCheck();
        dayOfMonthCheck.initialize(new DayOfMonth() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return null;
            }

            @Override
            public String message() {
                return null;
            }

            @Override
            public Class<?>[] groups() {
                return null;
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return null;
            }

            @Override
            public int[] value() {
                return new int[] {2, 4};
            }
        });
        assertFalse(dayOfMonthCheck.isValid(LocalDate.of(2020, 1, 1), null));
        assertTrue(dayOfMonthCheck.isValid(LocalDate.of(2020, 1, 2), null));
        assertFalse(dayOfMonthCheck.isValid(LocalDate.of(2020, 1, 3), null));
        assertTrue(dayOfMonthCheck.isValid(LocalDate.of(2020, 1, 4), null));
    }
}
