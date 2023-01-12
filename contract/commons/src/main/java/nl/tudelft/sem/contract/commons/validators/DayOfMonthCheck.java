package nl.tudelft.sem.contract.commons.validators;

import java.time.LocalDate;
import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates that the day of the month of a {@link LocalDate} is one of the specified days.
 */
public class DayOfMonthCheck implements ConstraintValidator<DayOfMonth, LocalDate> {
    private transient int[] daysOfMonth;

    @Override
    public void initialize(DayOfMonth constraintAnnotation) {
        this.daysOfMonth = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Arrays.stream(daysOfMonth).anyMatch(day -> day == value.getDayOfMonth());
    }
}
