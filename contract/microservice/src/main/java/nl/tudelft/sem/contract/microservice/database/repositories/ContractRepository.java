package nl.tudelft.sem.contract.microservice.database.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import nl.tudelft.sem.contract.microservice.database.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface ContractRepository extends JpaRepository<Contract, UUID> {
    @Query("SELECT c FROM Contract c WHERE c.status = nl.tudelft.sem.contract.commons.entities.ContractStatus.ACTIVE"
            + " AND (c.lastSalaryIncreaseDate IS NULL OR c.lastSalaryIncreaseDate <= ?1)"
            + " AND c.salaryScalePoint < 1.0")
    List<Contract> findContractEligibleForSalaryScaleIncrease(@NonNull LocalDate lastIncreaseBefore);

    @Query("SELECT c FROM Contract c WHERE c.status = nl.tudelft.sem.contract.commons.entities.ContractStatus.ACTIVE"
            + " AND c.endDate = ?1")
    List<Contract> findContractsNearExpiration(@NonNull LocalDate expirationDate);
}