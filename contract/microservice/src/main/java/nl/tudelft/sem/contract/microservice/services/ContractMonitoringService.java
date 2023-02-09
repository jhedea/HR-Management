package nl.tudelft.sem.contract.microservice.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.contract.microservice.database.entities.Contract;
import nl.tudelft.sem.contract.microservice.database.repositories.ContractRepository;
import nl.tudelft.sem.notification.client.NotificationClient;
import nl.tudelft.sem.notification.commons.entities.MessageDto;
import nl.tudelft.sem.notification.commons.entities.MessagePriority;
import nl.tudelft.sem.notification.commons.entities.UuidDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ContractMonitoringService {
    private final transient ContractRepository contractRepository;
    private final transient NotificationClient notificationClient;

    public ContractMonitoringService(ContractRepository contractRepository, NotificationClient notificationClient) {
        this.contractRepository = contractRepository;
        this.notificationClient = notificationClient;
    }

    /**
     * Check if any contracts are eligible for a salary increase.
     */
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    public void increaseApplicableSalaryScalePoints() {
        List<Contract> eligibleContracts = contractRepository
                .findContractEligibleForSalaryScaleIncrease(LocalDate.now().minusMonths(12));
        for (Contract contract : eligibleContracts) {
            contract.getContractTerms().setSalaryScalePoint(BigDecimal.ONE.min(
                    contract.getContractTerms().getSalaryScalePoint().add(
                                    contract.getJobPosition().getSalaryScale().getStep())));
            contract.getContractTerms().setLastSalaryIncreaseDate(LocalDate.now());
            try {
                contractRepository.save(contract);
            } catch (Exception e) {
                log.error("Failed to save contract with id {} after salary scale increase", contract.getId(), e);
            }
        }
    }

    /**
     * Check if any contracts are about to expire and send them an notification.
     */
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    public void sendContractExpirationNotifications() {
        List<Contract> contracts = contractRepository.findContractsNearExpiration(LocalDate.now().plusMonths(2));
        for (Contract contract : contracts) {
            try {
                UuidDto msgId = notificationClient.notification().sendMessage(
                        MessageDto.builder()
                                .setUserId(contract.getContractParties().getEmployeeId())
                                .setPriority(MessagePriority.MEDIUM)
                                .setSubject("Contract expiration")
                                .setMessage("Your contract is about to expire.")
                                .build()).join();
                log.info("Sent contract expiration notification to user {} with message id {}",
                        contract.getContractParties().getEmployeeId(), msgId);
            } catch (Exception e) {
                log.error("Failed to send contract expiration notification for contract with id {}", contract.getId(), e);
            }
        }
    }
}
