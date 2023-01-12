package nl.tudelft.sem.contract.microservice;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/**
 * Common functionality used in testing.
 */
public class TestHelpers {
    /**
     * Generate a UUID from a string.
     *
     * @param value the string to generate the UUID from
     * @return a UUID generated from the string
     */
    private static UUID getUuid(String value) {
        if (value.length() > 12) {
            throw new IllegalArgumentException("The number (converted to decimal) can be at most 12 characters long");
        }
        return UUID.fromString(
                String.format("00000000-0000-0000-0000-%s", StringUtils.leftPad(value, 12, '0')));
    }

    /**
     * Generate a UUID from a positive integer.
     *
     * @param i the number to generate a UUID from.
     * @return a UUID generated from the number.
     */
    public static UUID getUuid(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("The number must be positive");
        }
        return getUuid(String.valueOf(i));
    }
}
