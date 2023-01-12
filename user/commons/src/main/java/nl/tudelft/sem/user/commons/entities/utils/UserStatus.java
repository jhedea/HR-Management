package nl.tudelft.sem.user.commons.entities.utils;

public enum UserStatus {
    /**
     * When an account is initially created for a new candidate.
     */
    CANDIDATE,
    /**
     * In case a candidate doesn't get the job, but we still want to
     * keep the user information in the system instead of just deleting.
     */
    EX_CANDIDATE,
    /**
     * For working employees.
     */
    EMPLOYEE,
    /**
     * For ex-employees whose accounts we want to keep in the system.
     */
    FIRED,

    /**
     * For someone who quit based on their decision.
     */
    QUITTER
}
