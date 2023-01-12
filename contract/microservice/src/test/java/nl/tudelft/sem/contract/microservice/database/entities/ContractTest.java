package nl.tudelft.sem.contract.microservice.database.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import nl.tudelft.sem.contract.commons.entities.ContractDto;
import nl.tudelft.sem.contract.commons.entities.ContractStatus;
import nl.tudelft.sem.contract.commons.entities.ContractType;
import nl.tudelft.sem.contract.microservice.TestHelpers;
import org.junit.jupiter.api.Test;

class ContractTest {
    @Test
    void testConstructor() {
        ContractDto contractDto = new ContractDto();
        contractDto.setEmployeeId(TestHelpers.getUuid(1));
        contractDto.setEmployerId(TestHelpers.getUuid(3));
        contractDto.setType(ContractType.FULL_TIME);
        contractDto.setStatus(ContractStatus.ACTIVE);
        contractDto.setHoursPerWeek(10);
        contractDto.setVacationDays(20);
        contractDto.setStartDate(LocalDate.of(2020, 1, 1));
        contractDto.setEndDate(LocalDate.of(2020, 1, 2));
        contractDto.setTerminationDate(LocalDate.of(2020, 1, 3));
        contractDto.setSalaryScalePoint(new BigDecimal("0.5"));
        contractDto.setBenefits(List.of("benefit1", "benefit2"));

        JobPosition jobPosition = new JobPosition();
        jobPosition.setId(TestHelpers.getUuid(2));

        PensionScheme pensionScheme = new PensionScheme();
        pensionScheme.setId(TestHelpers.getUuid(4));

        Contract contract = new Contract(contractDto, jobPosition, pensionScheme);

        assertEquals(contractDto.getEmployeeId(), contract.getEmployeeId());
        assertEquals(contractDto.getEmployerId(), contract.getEmployerId());
        assertEquals(contractDto.getType(), contract.getType());
        assertEquals(contractDto.getStatus(), contract.getStatus());
        assertEquals(contractDto.getHoursPerWeek(), contract.getHoursPerWeek());
        assertEquals(contractDto.getVacationDays(), contract.getVacationDays());
        assertEquals(contractDto.getStartDate(), contract.getStartDate());
        assertEquals(contractDto.getEndDate(), contract.getEndDate());
        assertEquals(contractDto.getTerminationDate(), contract.getTerminationDate());
        assertEquals(contractDto.getBenefits(), contract.getBenefits());
        assertEquals(jobPosition.getId(), contract.getJobPosition().getId());
        assertEquals(contractDto.getSalaryScalePoint(), contract.getSalaryScalePoint());
        assertEquals(pensionScheme.getId(), contract.getPensionScheme().getId());
    }

    @Test
    void testToDto() {
        JobPosition jobPosition = new JobPosition();
        jobPosition.setId(TestHelpers.getUuid(2));

        PensionScheme pensionScheme = new PensionScheme();
        pensionScheme.setId(TestHelpers.getUuid(4));

        ContractDto contractDto = new ContractDto();
        contractDto.setEmployeeId(TestHelpers.getUuid(1));
        contractDto.setEmployerId(TestHelpers.getUuid(3));
        contractDto.setType(ContractType.FULL_TIME);
        contractDto.setStatus(ContractStatus.ACTIVE);
        contractDto.setHoursPerWeek(10);
        contractDto.setVacationDays(20);
        contractDto.setStartDate(LocalDate.of(2020, 1, 1));
        contractDto.setEndDate(LocalDate.of(2020, 1, 2));
        contractDto.setTerminationDate(LocalDate.of(2020, 1, 3));
        contractDto.setLastSalaryIncreaseDate(contractDto.getStartDate());
        contractDto.setBenefits(List.of("benefit1", "benefit2"));
        contractDto.setJobPosition(jobPosition.getDto());
        contractDto.setSalaryScalePoint(new BigDecimal("0.5"));
        contractDto.setPensionScheme(pensionScheme.getDto());

        Contract contract = new Contract(contractDto, jobPosition, pensionScheme);
        assertEquals(contractDto, contract.getDto());
    }

    @Test
    void testIsActive() {
        ContractDto contractDto = new ContractDto();
        contractDto.setEmployeeId(TestHelpers.getUuid(1));
        contractDto.setEmployerId(TestHelpers.getUuid(3));
        contractDto.setType(ContractType.FULL_TIME);
        contractDto.setStatus(ContractStatus.ACTIVE);
        contractDto.setHoursPerWeek(10);
        contractDto.setVacationDays(20);
        contractDto.setStartDate(LocalDate.of(2020, 1, 1));
        contractDto.setEndDate(LocalDate.of(2020, 1, 2));
        contractDto.setTerminationDate(LocalDate.of(2020, 1, 3));
        contractDto.setSalaryScalePoint(new BigDecimal("0.5"));
        contractDto.setBenefits(List.of("benefit1", "benefit2"));

        JobPosition jobPosition = new JobPosition();
        jobPosition.setId(TestHelpers.getUuid(2));

        PensionScheme pensionScheme = new PensionScheme();
        pensionScheme.setId(TestHelpers.getUuid(4));

        Contract contract = new Contract(contractDto, jobPosition, pensionScheme);
        assertTrue(contract.isActive());
    }
}