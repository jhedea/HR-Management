package nl.tudelft.sem.contract.commons.entities;

/**
 * Status of contract.
 */
public enum ContractStatus {
    /**
     * Contract is still under negotiation.
     */
    DRAFT,
    /**
     * Contract is active.
     */
    ACTIVE,
    /**
     * Contract duration has expired.
     */
    EXPIRED,
    /**
     * Contract was terminated.
     */
    TERMINATED,
}
