package nl.tudelft.sem.request.microservice.database.entities.utils;

import nl.tudelft.sem.request.commons.entities.RequestStatus;
import nl.tudelft.sem.request.microservice.database.entities.GeneralRequest;
import org.springframework.data.jpa.domain.Specification;

public class RequestSpecification {
    /**
     * Creates a specification for a request with a specific status.
     *
     * @param attribute attribute to filter on
     * @return a specification that filters on the given attribute
     */
    public static Specification<GeneralRequest> hasAttribute(RequestStatus attribute) {
        return (root, query, cb) -> cb.equal(root.get("status"), attribute);
    }
}
