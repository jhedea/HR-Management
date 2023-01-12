package nl.tudelft.sem.contract.commons.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Enforces that the day of the month of a {@link LocalDate} is one of the specified days.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE_USE})
@Documented
@Constraint(validatedBy = DayOfMonthCheck.class)
public @interface DayOfMonth {
    /**
     * The message to return when the validation fails.
     *
     * @return the error message template.
     */
    String message() default "Invalid day of month";

    /**
     * The groups the constraint belongs to.
     *
     * @return the groups the constraint belongs to.
     */
    Class<?>[] groups() default { };

    /**
     * Get the payload associated to the constraint.
     *
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default { };

    /**
     * Get the allowed days of the month.
     *
     * @return the allowed days of the month.
     */
    int[] value();
}
