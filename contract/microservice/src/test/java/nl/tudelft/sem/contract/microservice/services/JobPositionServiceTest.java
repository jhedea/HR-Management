package nl.tudelft.sem.contract.microservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import nl.tudelft.sem.contract.commons.entities.SalaryScaleDto;
import nl.tudelft.sem.contract.commons.entities.utils.StringDto;
import nl.tudelft.sem.contract.microservice.TestHelpers;
import nl.tudelft.sem.contract.microservice.database.entities.JobPosition;
import nl.tudelft.sem.contract.microservice.database.entities.SalaryScale;
import nl.tudelft.sem.contract.microservice.database.repositories.JobPositionRepository;
import nl.tudelft.sem.contract.microservice.database.repositories.SalaryScaleRepository;
import nl.tudelft.sem.contract.microservice.exceptions.SalaryScaleNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JobPositionServiceTest {
    @Mock
    private JobPositionRepository jobPositionRepository;
    @Mock
    private SalaryScaleRepository salaryScaleRepository;
    @InjectMocks
    private JobPositionService jobPositionService;

    @Test
    void editName() {
        JobPosition jobPosition = JobPosition.builder()
                .id(TestHelpers.getUuid(1))
                .name("CEO")
                .build();
        when(jobPositionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        JobPosition result = jobPositionService.editName(jobPosition, new StringDto("CFO"));

        verify(jobPositionRepository, times(1)).save(any());
        assertEquals(result.getId(), jobPosition.getId());
        assertEquals(result.getName(), "CFO");
        assertEquals(result.getSalaryScale(), jobPosition.getSalaryScale());
    }

    @Test
    void editSalaryScale() {
        SalaryScale salaryScale = SalaryScale.builder()
                .id(TestHelpers.getUuid(1))
                .minimumPay(new BigDecimal("1700.45"))
                .maximumPay(new BigDecimal("3000.00"))
                .step(new BigDecimal("0.01"))
                .build();

        JobPosition jobPosition = JobPosition.builder()
                .id(TestHelpers.getUuid(1))
                .name("CEO")
                .build();

        when(jobPositionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(salaryScaleRepository.findById(TestHelpers.getUuid(1))).thenReturn(Optional.of(salaryScale));

        JobPosition result = jobPositionService.editSalaryScale(jobPosition, salaryScale.getDto());

        verify(jobPositionRepository, times(1)).save(any());
        assertEquals(result.getId(), jobPosition.getId());
        assertEquals(result.getName(), jobPosition.getName());
        assertEquals(result.getSalaryScale(), salaryScale);
    }

    @Test
    void editSalaryScaleEmpty() {
        SalaryScaleDto newSalaryScale = SalaryScaleDto.builder()
                .minimumPay(new BigDecimal("123.00"))
                .maximumPay(new BigDecimal("234.00"))
                .step(new BigDecimal("0.1"))
                .build();

        JobPosition jobPosition = JobPosition.builder()
                .id(TestHelpers.getUuid(1))
                .name("CEO")
                .build();

        when(salaryScaleRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(SalaryScaleNotFoundException.class,
                () -> jobPositionService.editSalaryScale(jobPosition, newSalaryScale));
    }
}
