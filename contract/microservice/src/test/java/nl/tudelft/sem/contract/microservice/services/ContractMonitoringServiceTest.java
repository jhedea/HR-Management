package nl.tudelft.sem.contract.microservice.services;

import static nl.tudelft.sem.contract.microservice.TestHelpers.getUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import nl.tudelft.sem.contract.commons.entities.ContractStatus;
import nl.tudelft.sem.contract.commons.entities.ContractType;
import nl.tudelft.sem.contract.microservice.TestHelpers;
import nl.tudelft.sem.contract.microservice.database.entities.Contract;
import nl.tudelft.sem.contract.microservice.database.entities.JobPosition;
import nl.tudelft.sem.contract.microservice.database.entities.SalaryScale;
import nl.tudelft.sem.contract.microservice.database.repositories.ContractRepository;
import nl.tudelft.sem.notification.client.NotificationClient;
import nl.tudelft.sem.notification.client.NotificationData;
import nl.tudelft.sem.notification.commons.entities.MessageDto;
import nl.tudelft.sem.notification.commons.entities.UuidDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContractMonitoringServiceTest {
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private NotificationClient notificationClient;
    @Mock
    private NotificationData notificationData;
    @InjectMocks
    private ContractMonitoringService contractMonitoringService;
    @Captor
    private ArgumentCaptor<Contract> contractCaptor;
    @Captor
    private ArgumentCaptor<MessageDto> messageCaptor;

    @Test
    void testNoContracts() {
        when(contractRepository.findContractEligibleForSalaryScaleIncrease(any())).thenReturn(List.of());
        contractMonitoringService.increaseApplicableSalaryScalePoints();

        verify(contractRepository, never()).save(any());
    }

    @Test
    void testContractsPresent() {
        SalaryScale salaryScale = SalaryScale.builder()
                .id(TestHelpers.getUuid(1))
                .minimumPay(new BigDecimal("1000000.00"))
                .maximumPay(new BigDecimal("10000000.00"))
                .step(new BigDecimal("0.1"))
                .build();

        Contract contract1 = Contract.builder()
                .id(getUuid(1))
                .employeeId(getUuid(2))
                .employerId(getUuid(3))
                .type(ContractType.TEMPORARY)
                .status(ContractStatus.DRAFT)
                .hoursPerWeek(10)
                .vacationDays(20)
                .startDate(LocalDate.of(2022, 1, 1))
                .lastSalaryIncreaseDate(LocalDate.of(2020, 1, 1))
                .benefits(List.of())
                .jobPosition(JobPosition.builder()
                        .id(TestHelpers.getUuid(3))
                        .salaryScale(salaryScale)
                        .name("Job position").build())
                .salaryScalePoint(new BigDecimal("0.5"))
                .build();

        Contract contract2 = Contract.builder()
                .id(getUuid(2))
                .employeeId(getUuid(3))
                .employerId(getUuid(4))
                .type(ContractType.TEMPORARY)
                .status(ContractStatus.DRAFT)
                .hoursPerWeek(10)
                .vacationDays(20)
                .startDate(LocalDate.of(2022, 1, 1))
                .lastSalaryIncreaseDate(LocalDate.of(2020, 1, 1))
                .benefits(List.of())
                .jobPosition(JobPosition.builder()
                        .id(TestHelpers.getUuid(3))
                        .salaryScale(salaryScale)
                        .name("Job position").build())
                .salaryScalePoint(new BigDecimal("0.5"))
                .build();

        Contract contract3 = Contract.builder()
                .id(getUuid(3))
                .employeeId(getUuid(3))
                .employerId(getUuid(4))
                .type(ContractType.TEMPORARY)
                .status(ContractStatus.DRAFT)
                .hoursPerWeek(10)
                .vacationDays(20)
                .startDate(LocalDate.of(2022, 1, 1))
                .lastSalaryIncreaseDate(LocalDate.of(2020, 1, 1))
                .benefits(List.of())
                .jobPosition(JobPosition.builder()
                        .id(TestHelpers.getUuid(3))
                        .salaryScale(salaryScale)
                        .name("Job position").build())
                .salaryScalePoint(new BigDecimal("0.95"))
                .build();

        when(contractRepository.findContractEligibleForSalaryScaleIncrease(any()))
                .thenReturn(List.of(contract1, contract2, contract3));
        when(contractRepository.save(contractCaptor.capture()))
                .thenThrow(new RuntimeException())
                .thenAnswer(invocation -> invocation.getArgument(0));

        contractMonitoringService.increaseApplicableSalaryScalePoints();

        verify(contractRepository, times(3)).save(any());
        assertEquals(new BigDecimal("0.6"), contractCaptor.getAllValues().get(0).getSalaryScalePoint());
        assertEquals(getUuid(1), contractCaptor.getAllValues().get(0).getId());

        assertEquals(new BigDecimal("0.6"), contractCaptor.getAllValues().get(1).getSalaryScalePoint());
        assertEquals(getUuid(2), contractCaptor.getAllValues().get(1).getId());

        assertEquals(new BigDecimal("1"), contractCaptor.getAllValues().get(2).getSalaryScalePoint());
        assertEquals(getUuid(3), contractCaptor.getAllValues().get(2).getId());
    }

    @Test
    void testSendExpirationNoContracts() throws JsonProcessingException {
        when(contractRepository.findContractsNearExpiration(any())).thenReturn(List.of());
        lenient().when(notificationClient.notification()).thenReturn(notificationData);
        contractMonitoringService.sendContractExpirationNotifications();

        verify(notificationData, never()).sendMessage(any());
    }

    @Test
    void testSendExpirationContractsPresent() throws JsonProcessingException {
        SalaryScale salaryScale = SalaryScale.builder()
                .id(TestHelpers.getUuid(1))
                .minimumPay(new BigDecimal("1000000.00"))
                .maximumPay(new BigDecimal("10000000.00"))
                .step(new BigDecimal("0.1"))
                .build();

        Contract contract1 = Contract.builder()
                .id(getUuid(1))
                .employeeId(getUuid(2))
                .employerId(getUuid(3))
                .type(ContractType.TEMPORARY)
                .status(ContractStatus.DRAFT)
                .hoursPerWeek(10)
                .vacationDays(20)
                .startDate(LocalDate.of(2022, 1, 1))
                .lastSalaryIncreaseDate(LocalDate.of(2020, 1, 1))
                .benefits(List.of())
                .jobPosition(JobPosition.builder()
                        .id(TestHelpers.getUuid(3))
                        .salaryScale(salaryScale)
                        .name("Job position").build())
                .salaryScalePoint(new BigDecimal("0.5"))
                .build();

        Contract contract2 = Contract.builder()
                .id(getUuid(2))
                .employeeId(getUuid(3))
                .employerId(getUuid(4))
                .type(ContractType.TEMPORARY)
                .status(ContractStatus.DRAFT)
                .hoursPerWeek(10)
                .vacationDays(20)
                .startDate(LocalDate.of(2022, 1, 1))
                .lastSalaryIncreaseDate(LocalDate.of(2020, 1, 1))
                .benefits(List.of())
                .jobPosition(JobPosition.builder()
                        .id(TestHelpers.getUuid(3))
                        .salaryScale(salaryScale)
                        .name("Job position").build())
                .salaryScalePoint(new BigDecimal("0.5"))
                .build();

        Contract contract3 = Contract.builder()
                .id(getUuid(3))
                .employerId(getUuid(4))
                .employeeId(getUuid(4))
                .type(ContractType.TEMPORARY)
                .status(ContractStatus.DRAFT)
                .hoursPerWeek(10)
                .vacationDays(20)
                .startDate(LocalDate.of(2022, 1, 1))
                .lastSalaryIncreaseDate(LocalDate.of(2020, 1, 1))
                .benefits(List.of())
                .jobPosition(JobPosition.builder()
                        .id(TestHelpers.getUuid(3))
                        .salaryScale(salaryScale)
                        .name("Job position").build())
                .salaryScalePoint(new BigDecimal("0.95"))
                .build();

        lenient().when(notificationClient.notification()).thenReturn(notificationData);
        when(contractRepository.findContractsNearExpiration(any()))
                .thenReturn(List.of(contract1, contract2, contract3));
        when(notificationData.sendMessage(messageCaptor.capture()))
                .thenThrow(new RuntimeException())
                .thenAnswer(invocation -> new UuidDto(((MessageDto) invocation.getArgument(0)).getId()));

        contractMonitoringService.sendContractExpirationNotifications();

        verify(notificationData, times(3)).sendMessage(any());
        assertEquals(getUuid(2), messageCaptor.getAllValues().get(0).getUserId());
        assertEquals(getUuid(3), messageCaptor.getAllValues().get(1).getUserId());
        assertEquals(getUuid(4), messageCaptor.getAllValues().get(2).getUserId());
    }
}