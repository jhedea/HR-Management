package nl.tudelft.sem.contract.microservice.database.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import nl.tudelft.sem.contract.commons.entities.SalaryScaleDto;
import org.junit.jupiter.api.Test;

class SalaryScaleTest {
    @Test
    void testDtoConstructor() {
        SalaryScaleDto dto = SalaryScaleDto.builder()
                .minimumPay(new BigDecimal("60.34"))
                .maximumPay(new BigDecimal("4304.43"))
                .step(new BigDecimal("0.01"))
                .build();
        SalaryScale salaryScale = new SalaryScale(dto);
        assertEquals(salaryScale.getPay().getMinimumPay(), dto.getMinimumPay());
        assertEquals(salaryScale.getPay().getMaximumPay(), dto.getMaximumPay());
        assertEquals(salaryScale.getStep(), dto.getStep());
    }

    @Test
    void testDtoConstructorInvalidSalaries() {
        SalaryScaleDto dto = SalaryScaleDto.builder()
                .minimumPay(new BigDecimal("4304.43"))
                .maximumPay(new BigDecimal("60.34"))
                .step(new BigDecimal("0.01"))
                .build();
        assertThrows(IllegalArgumentException.class, () -> new SalaryScale(dto));
    }
}