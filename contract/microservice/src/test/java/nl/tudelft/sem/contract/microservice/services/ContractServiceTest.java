package nl.tudelft.sem.contract.microservice.services;

import static nl.tudelft.sem.contract.microservice.TestHelpers.getUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
                .employeeId(getUuid(2))
                .employerId(getUuid(3))
                .type(ContractType.TEMPORARY)
                .status(ContractStatus.DRAFT)
                .hoursPerWeek(10)
                .vacationDays(20)
                .startDate(LocalDate.of(2022, 1, 1))
                .benefits(List.of())
                .jobPosition(JobPosition.builder().id(TestHelpers.getUuid(3)).name("Job position").build())
                .lastSalaryIncreaseDate(LocalDate.of(2022, 1, 1))
                .salaryScalePoint(new BigDecimal("0.5"))
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
        assertEquals(result.getVacationDays(), 15);
        assertEquals(result.getSalaryScalePoint(), new BigDecimal("0.4"));
        assertEquals(result.getHoursPerWeek(), 10);
        assertEquals(result.getType(), ContractType.PART_TIME);
        assertEquals(result.getEndDate(), LocalDate.of(2022, 1, 1));
        assertEquals(result.getBenefits(), List.of("benefit1", "benefit2"));
        assertEquals(result.getJobPosition(), newJobPosition);
        assertEquals(result.getStartDate(), LocalDate.of(2022, 1, 15));
    }

    @Test
    void modifyContractEmpty() {
        Contract contract = Contract.builder()
                .id(getUuid(1))
                .employeeId(getUuid(2))
                .employerId(getUuid(3))
                .type(ContractType.TEMPORARY)
                .status(ContractStatus.DRAFT)
                .hoursPerWeek(10)
                .vacationDays(20)
                .startDate(LocalDate.of(2022, 1, 1))
                .benefits(List.of())
                .jobPosition(JobPosition.builder().id(TestHelpers.getUuid(3)).name("Job position").build())
                .salaryScalePoint(new BigDecimal("0.5"))
                .lastSalaryIncreaseDate(LocalDate.of(2022, 1, 1))
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
                .employeeId(getUuid(2))
                .employerId(getUuid(3))
                .type(ContractType.TEMPORARY)
                .status(ContractStatus.EXPIRED)
                .hoursPerWeek(10)
                .vacationDays(20)
                .startDate(LocalDate.of(2022, 1, 1))
                .benefits(List.of())
                .lastSalaryIncreaseDate(LocalDate.of(2022, 1, 1))
                .jobPosition(JobPosition.builder().id(TestHelpers.getUuid(3)).name("Job position").build())
                .salaryScalePoint(new BigDecimal("0.5"))
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
                .employeeId(getUuid(2))
                .employerId(getUuid(3))
                .type(ContractType.TEMPORARY)
                .status(ContractStatus.DRAFT)
                .hoursPerWeek(10)
                .vacationDays(20)
                .startDate(LocalDate.of(2022, 1, 1))
                .benefits(List.of())
                .jobPosition(JobPosition.builder().id(TestHelpers.getUuid(3)).name("Job position").build())
                .salaryScalePoint(new BigDecimal("0.5"))
                .lastSalaryIncreaseDate(LocalDate.of(2022, 1, 1))
                .build();
        when(contractRepository.findById(getUuid(1))).thenReturn(Optional.of(contract));
        assertThrows(ActionNotAllowedException.class, () -> contractService.terminateContract(getUuid(1)));
    }

    @Test
    void terminateActiveContract() {
        Contract contract = Contract.builder()
                .id(getUuid(1))
                .employeeId(getUuid(2))
                .employerId(getUuid(3))
                .type(ContractType.TEMPORARY)
                .status(ContractStatus.ACTIVE)
                .hoursPerWeek(10)
                .vacationDays(20)
                .startDate(LocalDate.of(2022, 1, 1))
                .benefits(List.of())
                .jobPosition(JobPosition.builder().id(TestHelpers.getUuid(3)).name("Job position").build())
                .salaryScalePoint(new BigDecimal("0.5"))
                .lastSalaryIncreaseDate(LocalDate.of(2022, 1, 1))
                .build();
        when(contractRepository.findById(getUuid(1))).thenReturn(Optional.of(contract));
        when(contractRepository.save(contractCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        contractService.terminateContract(getUuid(1));

        verify(contractRepository, times(1)).save(contract);
        assertEquals(contractCaptor.getValue().getStatus(), ContractStatus.TERMINATED);
    }
}