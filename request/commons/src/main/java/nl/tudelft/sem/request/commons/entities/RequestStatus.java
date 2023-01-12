package nl.tudelft.sem.request.commons.entities;

public enum RequestStatus {
    /**
     * Active requests.
     */
    OPEN,

    /**
     * Approved closed requests.
     */
    APPROVED,

    /**
     * Rejected closed requests.
     */
    REJECTED,
}
