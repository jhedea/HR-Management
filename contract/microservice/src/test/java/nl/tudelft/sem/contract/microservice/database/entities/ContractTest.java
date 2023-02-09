package nl.tudelft.sem.contract.microservice.database.entities;

import static nl.tudelft.sem.contract.microservice.TestHelpers.getUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import nl.tudelft.sem.contract.commons.entities.ContractDto;
import nl.tudelft.sem.contract.commons.entities.ContractInfoDto;
import nl.tudelft.sem.contract.commons.entities.ContractModificationDto;
import nl.tudelft.sem.contract.commons.entities.ContractPartiesDto;
import nl.tudelft.sem.contract.commons.entities.ContractStatus;
import nl.tudelft.sem.contract.commons.entities.ContractTermsDto;
import nl.tudelft.sem.contract.commons.entities.ContractType;
import nl.tudelft.sem.contract.microservice.TestHelpers;
import nl.tudelft.sem.contract.microservice.exceptions.ActionNotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ContractTest {
    @Test
    void testConstructor() {

        JobPosition jobPosition = new JobPosition();
        jobPosition.setId(getUuid(2));

        PensionScheme pensionScheme = new PensionScheme();
        pensionScheme.setId(getUuid(4));

        ContractDto contractDto = ContractDto.builder()
                .contractInfo(ContractInfoDto.builder().status(ContractStatus.ACTIVE).type(ContractType.FULL_TIME).build())
                .contractParties(new ContractPartiesDto(getUuid(1), getUuid(3)))
                .contractTerms(ContractTermsDto.builder()
                        .hoursPerWeek(10)
                        .vacationDays(20)
                        .lastSalaryIncreaseDate(LocalDate.of(2020, 1, 1))
                        .salaryScalePoint(new BigDecimal("0.5"))
                        .startDate(LocalDate.of(2020, 1, 1))
                        .endDate(LocalDate.of(2020, 1, 2))
                        .terminationDate(LocalDate.of(2020, 1, 3)).build())
                .benefits(List.of("benefit1", "benefit2"))
                .jobPosition(jobPosition.getDto())
                .pensionScheme(pensionScheme.getDto())
                .build();

        Contract contract = new Contract(contractDto, jobPosition, pensionScheme);

        assertEquals(contractDto.getContractParties().getEmployeeId(), contract.getContractParties().getEmployeeId());
        assertEquals(contractDto.getContractParties().getEmployerId(), contract.getContractParties().getEmployerId());
        assertEquals(contractDto.getContractInfo().getType(), contract.getContractInfo().getType());
        assertEquals(contractDto.getContractInfo().getStatus(), contract.getContractInfo().getStatus());
        assertEquals(contractDto.getContractTerms().getHoursPerWeek(), contract.getContractTerms().getHoursPerWeek());
        assertEquals(contractDto.getContractTerms().getVacationDays(), contract.getContractTerms().getVacationDays());
        assertEquals(contractDto.getContractTerms().getStartDate(), contract.getContractTerms().getStartDate());
        assertEquals(contractDto.getContractTerms().getEndDate(), contract.getContractTerms().getEndDate());
        assertEquals(contractDto.getContractTerms().getTerminationDate(), contract.getContractTerms().getTerminationDate());
        assertEquals(contractDto.getBenefits(), contract.getBenefits());
        assertEquals(jobPosition.getId(), contract.getJobPosition().getId());
        assertEquals(contractDto.getContractTerms().getSalaryScalePoint(),
                contract.getContractTerms().getSalaryScalePoint());
        assertEquals(pensionScheme.getId(), contract.getPensionScheme().getId());
    }

    @Test
    void testToDto() {
        JobPosition jobPosition = new JobPosition();
        jobPosition.setId(TestHelpers.getUuid(2));

        PensionScheme pensionScheme = new PensionScheme();
        pensionScheme.setId(TestHelpers.getUuid(4));

        ContractDto contractDto = ContractDto.builder()
                .contractInfo(new ContractInfoDto(ContractType.FULL_TIME, ContractStatus.ACTIVE))
                .contractParties(new ContractPartiesDto(getUuid(1), getUuid(3)))
                .contractTerms(ContractTermsDto.builder()
                        .hoursPerWeek(10)
                        .vacationDays(20)
                        .lastSalaryIncreaseDate(LocalDate.of(2020, 1, 1))
                        .salaryScalePoint(new BigDecimal("0.5"))
                        .startDate(LocalDate.of(2020, 1, 1))
                        .endDate(LocalDate.of(2020, 1, 2))
                        .terminationDate(LocalDate.of(2020, 1, 3)).build())
                .benefits(List.of("benefit1", "benefit2"))
                .jobPosition(jobPosition.getDto())
                .pensionScheme(pensionScheme.getDto())
                .build();

        Contract contract = new Contract(contractDto, jobPosition, pensionScheme);
        assertEquals(contractDto.getBenefits(), contract.getDto().getBenefits());
        assertEquals(contractDto, contract.getDto());
    }

    @Test
    void testIsActive() {


        JobPosition jobPosition = new JobPosition();
        jobPosition.setId(getUuid(2));

        PensionScheme pensionScheme = new PensionScheme();
        pensionScheme.setId(getUuid(4));

        ContractDto contractDto = ContractDto.builder()
                .contractInfo(ContractInfoDto.builder().status(ContractStatus.ACTIVE).type(ContractType.FULL_TIME).build())
                .contractParties(new ContractPartiesDto(getUuid(1), getUuid(3)))
                .contractTerms(ContractTermsDto.builder()
                        .hoursPerWeek(10)
                        .vacationDays(20)
                        .lastSalaryIncreaseDate(LocalDate.of(2020, 1, 1))
                        .salaryScalePoint(new BigDecimal("0.5"))
                        .startDate(LocalDate.of(2020, 1, 1))
                        .endDate(LocalDate.of(2020, 1, 2))
                        .terminationDate(LocalDate.of(2020, 1, 3)).build())
                .benefits(List.of("benefit1", "benefit2"))
                .jobPosition(jobPosition.getDto())
                .pensionScheme(pensionScheme.getDto())
                .build();

        Contract contract = new Contract(contractDto, jobPosition, pensionScheme);
        assertTrue(contract.isActive());
    }

    @Nested
    @DisplayName("modifyDraft Tests")
    class ModifyDraftTests {
        private ContractModificationDto modifications;
        private Contract contract;
        private JobPosition jobPosition;

        @BeforeEach
        public void beforeEach() {
            modifications = new ContractModificationDto();

            jobPosition = new JobPosition();
            jobPosition.setId(getUuid(2));

            PensionScheme pensionScheme = new PensionScheme();
            pensionScheme.setId(getUuid(4));

            ContractDto contractDto = ContractDto.builder()
                    .contractInfo(
                            ContractInfoDto.builder()
                                    .status(ContractStatus.DRAFT)
                                    .type(ContractType.FULL_TIME)
                                    .build()
                    )
                    .contractParties(new ContractPartiesDto(getUuid(1), getUuid(3)))
                    .contractTerms(ContractTermsDto.builder()
                            .hoursPerWeek(10)
                            .vacationDays(20)
                            .lastSalaryIncreaseDate(LocalDate.of(2020, 1, 1))
                            .salaryScalePoint(new BigDecimal("0.5"))
                            .startDate(LocalDate.of(2020, 1, 1))
                            .endDate(LocalDate.of(2020, 1, 2))
                            .terminationDate(LocalDate.of(2020, 1, 3)).build())
                    .benefits(List.of("benefit1", "benefit2"))
                    .jobPosition(jobPosition.getDto())
                    .pensionScheme(pensionScheme.getDto())
                    .build();

            contract = new Contract(contractDto, jobPosition, pensionScheme);

            modifications.setHoursPerWeek(40);
        }

        @Test
        void modifyDraft_Valid() {
            contract.modifyDraft(modifications, jobPosition);

            assertEquals(contract.getContractTerms().getHoursPerWeek(), 40);
        }

        @Test
        void modifyDraft_Invalid() {
            contract.getContractInfo().setStatus(ContractStatus.ACTIVE);

            ActionNotAllowedException thrown = assertThrows(ActionNotAllowedException.class, () -> {
                contract.modifyDraft(modifications, jobPosition);
            });

            assertEquals(thrown.getMessage(), "Contract is not in draft, thus cannot be modified.");
        }
    }
}