package nl.tudelft.sem.contract.microservice.services;

import static nl.tudelft.sem.contract.microservice.TestHelpers.getUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.contract.commons.entities.ContractModificationDto;
import nl.tudelft.sem.contract.commons.entities.ContractStatus;
import nl.tudelft.sem.contract.commons.entities.ContractType;
import nl.tudelft.sem.contract.microservice.TestHelpers;
import nl.tudelft.sem.contract.microservice.database.entities.Contract;
import nl.tudelft.sem.contract.microservice.database.entities.JobPosition;
import nl.tudelft.sem.contract.microservice.database.entities.contract.ContractInfo;
import nl.tudelft.sem.contract.microservice.database.entities.contract.ContractParties;
import nl.tudelft.sem.contract.microservice.database.entities.contract.ContractTerms;
import nl.tudelft.sem.contract.microservice.database.repositories.ContractRepository;
import nl.tudelft.sem.contract.microservice.database.repositories.JobPositionRepository;
import nl.tudelft.sem.contract.microservice.exceptions.ActionNotAllowedException;
import nl.tudelft.sem.contract.microservice.exceptions.ContractNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private JobPositionRepository jobPositionRepository;
    @InjectMocks
    private ContractService contractService;

    @Captor
    private ArgumentCaptor<Contract> contractCaptor;

    @Test
    void modifyContract() {
        JobPosition newJobPosition = JobPosition.builder()
                .id(getUuid(1))
                .name("newJobPosition")
                .build();

        Contract contract = Contract.builder()
                .id(getUuid(1))
                .contractParties(new ContractParties(getUuid(2), getUuid(3)))
                .contractInfo(new ContractInfo(ContractType.TEMPORARY, ContractStatus.DRAFT))
                .contractTerms(ContractTerms.builder()
                        .hoursPerWeek(10)
                        .vacationDays(20)
                        .startDate(LocalDate.of(2022, 1, 1))
                        .lastSalaryIncreaseDate(LocalDate.of(2022, 1, 1))
                        .salaryScalePoint(new BigDecimal("0.5"))
                        .build())
                .benefits(List.of())
                .jobPosition(JobPosition.builder().id(TestHelpers.getUuid(3)).name("Job position").build())
                .build();

        ContractModificationDto modDto = ContractModificationDto.builder()
                .vacationDays(15)
                .salaryScalePoint(new BigDecimal("0.4"))
                .hoursPerWeek(10)
                .type(ContractType.PART_TIME)
                .endDate(LocalDate.of(2022, 1, 1))
                .benefits(List.of("benefit1", "benefit2"))
                .jobPosition("New job position")
                .startDate(LocalDate.of(2022, 1, 15))
                .build();
        when(jobPositionRepository.findByName("New job position")).thenReturn(Optional.of(newJobPosition));
        when(contractRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Contract result = contractService.modifyDraftContract(contract, modDto);

        verify(contractRepository, times(1)).save(any());
        assertEquals(result.getId(), contract.getId());
        assertEquals(result.getContractTerms().getVacationDays(), 15);
        assertEquals(result.getContractTerms().getSalaryScalePoint(), new BigDecimal("0.4"));
        assertEquals(result.getContractTerms().getHoursPerWeek(), 10);
        assertEquals(result.getContractInfo().getType(), ContractType.PART_TIME);
        assertEquals(result.getContractTerms().getEndDate(), LocalDate.of(2022, 1, 1));
        assertEquals(result.getBenefits(), List.of("benefit1", "benefit2"));
        assertEquals(result.getJobPosition(), newJobPosition);
        assertEquals(result.getContractTerms().getStartDate(), LocalDate.of(2022, 1, 15));
    }

    @Test
    void modifyContractEmpty() {
        Contract contract = Contract.builder()
                .id(getUuid(1))
                .contractParties(new ContractParties(getUuid(2), getUuid(3)))
                .contractInfo(new ContractInfo(ContractType.TEMPORARY, ContractStatus.DRAFT))
                .contractTerms(ContractTerms.builder()
                        .hoursPerWeek(10)
                        .vacationDays(20)
                        .startDate(LocalDate.of(2022, 1, 1))
                        .lastSalaryIncreaseDate(LocalDate.of(2022, 1, 1))
                        .salaryScalePoint(new BigDecimal("0.5"))
                        .build())
                .benefits(List.of())
                .jobPosition(JobPosition.builder().id(TestHelpers.getUuid(3)).name("Job position").build())
                .build();

        ContractModificationDto modDto = ContractModificationDto.builder().build();
        when(contractRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Contract result = contractService.modifyDraftContract(contract, modDto);

        verify(contractRepository, times(1)).save(any());
        assertEquals(result, contract);
    }

    @Test
    void modifyContractNotDraft() {
        Contract contract = Contract.builder()
                .id(getUuid(1))
                .contractParties(new ContractParties(getUuid(2), getUuid(3)))
                .contractInfo(new ContractInfo(ContractType.TEMPORARY, ContractStatus.EXPIRED))
                .contractTerms(ContractTerms.builder()
                        .hoursPerWeek(10)
                        .vacationDays(20)
                        .startDate(LocalDate.of(2022, 1, 1))
                        .salaryScalePoint(new BigDecimal("0.5"))
                        .build())
                .benefits(List.of())
                .jobPosition(JobPosition.builder().id(TestHelpers.getUuid(3)).name("Job position").build())
                .build();

        ContractModificationDto modDto = ContractModificationDto.builder().build();
        assertThrows(ActionNotAllowedException.class, () -> contractService.modifyDraftContract(contract, modDto));
    }

    @Test
    void terminateNonExistingContract() {
        when(contractRepository.findById(getUuid(1))).thenReturn(Optional.empty());
        assertThrows(ContractNotFoundException.class, () -> contractService.terminateContract(getUuid(1)));
    }

    @Test
    void terminateNonActiveContract() {
        Contract contract = Contract.builder()
                .id(getUuid(1))
                .contractParties(new ContractParties(getUuid(2), getUuid(3)))
                .contractInfo(new ContractInfo(ContractType.TEMPORARY, ContractStatus.DRAFT))
                .contractTerms(ContractTerms.builder()
                        .hoursPerWeek(10)
                        .vacationDays(20)
                        .startDate(LocalDate.of(2022, 1, 1))
                        .salaryScalePoint(new BigDecimal("0.5"))
                        .build())
                .benefits(List.of())
                .jobPosition(JobPosition.builder().id(TestHelpers.getUuid(3)).name("Job position").build())
                .build();
        when(contractRepository.findById(getUuid(1))).thenReturn(Optional.of(contract));
        assertThrows(ActionNotAllowedException.class, () -> contractService.terminateContract(getUuid(1)));
    }

    @Test
    void terminateActiveContract() {

        Contract contract = Contract.builder()
                .id(getUuid(1))
                .contractParties(new ContractParties(getUuid(2), getUuid(3)))
                .contractInfo(new ContractInfo(ContractType.TEMPORARY, ContractStatus.ACTIVE))
                .contractTerms(ContractTerms.builder()
                        .hoursPerWeek(10)
                        .vacationDays(20)
                        .startDate(LocalDate.of(2022, 1, 1))
                        .lastSalaryIncreaseDate(LocalDate.of(2022, 1, 1))
                        .salaryScalePoint(new BigDecimal("0.5"))
                        .build())
                .benefits(List.of())
                .jobPosition(JobPosition.builder().id(TestHelpers.getUuid(3)).name("Job position").build())
                .build();
        when(contractRepository.findById(getUuid(1))).thenReturn(Optional.of(contract));
        when(contractRepository.save(contractCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDate now = LocalDate.now();
        contractService.terminateContract(getUuid(1));
        assertNotNull(contractCaptor.getValue().getContractTerms().getTerminationDate());
        assertTrue(contractCaptor.getValue().getContractTerms().getTerminationDate().isAfter(now)
                || contractCaptor.getValue().getContractTerms().getTerminationDate().isEqual(now));
        verify(contractRepository, times(1)).save(contract);
        assertEquals(contractCaptor.getValue().getContractInfo().getStatus(), ContractStatus.TERMINATED);
    }
}
