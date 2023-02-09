package nl.tudelft.sem.request.microservice;

import java.time.LocalDateTime;
import java.util.UUID;
import nl.tudelft.sem.request.commons.entities.RequestStatus;
import nl.tudelft.sem.request.microservice.database.entities.GeneralRequest;

public class TestUtils {
    static final UUID id = UUID.fromString("58028d7a-21e7-461e-bef9-f9432ad611cc");

    /**
     * Generate a default request.
     *
     * @return Generated request.
     */
    public static GeneralRequest defaultRequest() {
        return GeneralRequest.builder()
                .id(id)
                .status(RequestStatus.OPEN)
                .requestBody("test request")
                .requestDate(LocalDateTime.of(1, 1, 0, 0, 0))
                .responseBody(null)
                .responseDate(null)
                .numberOfDays(0)
                .build();
    }

    public static UUID defaultUuid() {
        return id;
    }
}
