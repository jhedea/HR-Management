package nl.tudelft.sem.request.commons.entities;

public enum RequestType {
    /**
     * Any type of request.
     */
    GENERAL,

    /**
     * Request to terminate contract.
     */
    TERMINATION,

    /**
     * Request for vacation.
     */
    VACATION,

    /**
     * Request for special kinds of leave (Sick leave, Maternity Leave).
     */
    LEAVE,

    /**
     * Request for draft contract modification.
     */
    MODIFY,

    /**
     * Request to respond to a document request.
     */
    RESPONSE,

}
