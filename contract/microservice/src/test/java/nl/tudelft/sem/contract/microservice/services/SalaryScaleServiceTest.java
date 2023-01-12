package nl.tudelft.sem.contract.microservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import nl.tudelft.sem.contract.commons.entities.utils.StringDto;
import nl.tudelft.sem.contract.microservice.TestHelpers;
import nl.tudelft.sem.contract.microservice.database.entities.SalaryScale;
import nl.tudelft.sem.contract.microservice.database.repositories.SalaryScaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SalaryScaleServiceTest {
    @Mock
    private SalaryScaleRepository salaryScaleRepository;

    @InjectMocks
    private SalaryScaleService salaryScaleService;

    @Test
    void editMinimumPay() {
        SalaryScale salaryScale = SalaryScale.builder()
                .id(TestHelpers.getUuid(1))
                .minimumPay(new BigDecimal("60.34"))
                .maximumPay(new BigDecimal("4304.43"))
                .step(new BigDecimal("0.01"))
                .build();

        when(salaryScaleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        SalaryScale result = salaryScaleService.editMinimumPay(salaryScale, new StringDto("546.68"));

        verify(salaryScaleRepository, times(1)).save(any());
        assertEquals(result.getId(), salaryScale.getId());
        assertEquals(result.getMinimumPay(), new BigDecimal("546.68"));
        assertEquals(result.getMaximumPay(), salaryScale.getMaximumPay());
    }

    @Test
    void editMaximumPay() {
        SalaryScale salaryScale = SalaryScale.builder()
                .id(TestHelpers.getUuid(1))
                .minimumPay(new BigDecimal("60.34"))
                .maximumPay(new BigDecimal("4304.43"))
                .step(new BigDecimal("0.01"))
                .build();

        when(salaryScaleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        SalaryScale result = salaryScaleService.editMaximumPay(salaryScale, new StringDto("5546.68"));

        verify(salaryScaleRepository, times(1)).save(any());
        assertEquals(result.getId(), salaryScale.getId());
        assertEquals(result.getMinimumPay(), salaryScale.getMinimumPay());
        assertEquals(result.getMaximumPay(), new BigDecimal("5546.68"));
    }

}
