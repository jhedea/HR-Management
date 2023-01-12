package nl.tudelft.sem.contract.microservice.controllers.internal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.util.Optional;
import nl.tudelft.sem.contract.commons.entities.utils.StringDto;
import nl.tudelft.sem.contract.microservice.TestHelpers;
import nl.tudelft.sem.contract.microservice.database.entities.SalaryScale;
import nl.tudelft.sem.contract.microservice.database.repositories.SalaryScaleRepository;
import nl.tudelft.sem.contract.microservice.services.SalaryScaleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@ActiveProfiles(profiles = "testing")
public class SalaryScaleControllerTest {
    private final transient MockMvc mockMvc;
    private final transient ObjectMapper objectMapper;

    @MockBean
    private transient SalaryScaleService salaryScaleService;

    @MockBean
    private transient SalaryScaleRepository salaryScaleRepository;

    @Autowired
    SalaryScaleControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper().registerModules(new Jdk8Module(), new JavaTimeModule());
    }

    @Test
    void getNonExistentSalaryScale() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/internal/salary-scale/{id}", TestHelpers.getUuid(1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getExistentSalaryScale() throws Exception {
        SalaryScale salaryScale = SalaryScale.builder()
                .id(TestHelpers.getUuid(1))
                .minimumPay(new BigDecimal("1000000.00"))
                .maximumPay(new BigDecimal("10000000.00"))
                .step(new BigDecimal("0.01"))
                .build();

        when(salaryScaleRepository.findById(TestHelpers.getUuid(1))).thenReturn(Optional.of(salaryScale));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/internal/salary-scale/{id}", TestHelpers.getUuid(1)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(salaryScale.getDto())));
    }

    @Test
    void addNewSalaryScale() throws Exception {
        SalaryScale salaryScale = SalaryScale.builder()
                .id(TestHelpers.getUuid(1))
                .minimumPay(new BigDecimal("1000000.00"))
                .maximumPay(new BigDecimal("10000000.00"))
                .step(new BigDecimal("0.01"))
                .build();

        when(salaryScaleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/salary-scale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salaryScale.getDto())))
                .andExpect(status().isCreated());
    }

    @Test
    void editMinimumPayNonExistent() throws Exception {
        StringDto pay = new StringDto("7000.00");
        this.mockMvc.perform(MockMvcRequestBuilders.put("/internal/salary-scale/{id}/edit-minimum-pay",
                                TestHelpers.getUuid(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pay)))
                .andExpect(status().isNotFound());
    }

    @Test
    void editMinimumPayExistent() throws Exception {
        SalaryScale salaryScale = SalaryScale.builder()
                .id(TestHelpers.getUuid(1))
                .minimumPay(new BigDecimal("1000000.00"))
                .maximumPay(new BigDecimal("10000000.00"))
                .step(new BigDecimal("0.01"))
                .build();

        StringDto newPay = new StringDto("70");

        SalaryScale changedSalaryScale = SalaryScale.builder()
                .id(TestHelpers.getUuid(2))
                .minimumPay(new BigDecimal("70.00"))
                .maximumPay(new BigDecimal("10000000.00"))
                .step(new BigDecimal("0.01"))
                .build();

        when(salaryScaleRepository.findById(TestHelpers.getUuid(1))).thenReturn(Optional.of(salaryScale));
        when(salaryScaleService.editMinimumPay(salaryScale, newPay)).thenReturn(changedSalaryScale);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/internal/salary-scale/{id}/edit-minimum-pay",
                                TestHelpers.getUuid(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPay)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(changedSalaryScale.getDto())));
    }

    @Test
    void editMaximumPayNonExistent() throws Exception {
        StringDto pay = new StringDto("7000.00");
        this.mockMvc.perform(MockMvcRequestBuilders.put("/internal/salary-scale/{id}/edit-maximum-pay",
                                TestHelpers.getUuid(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pay)))
                .andExpect(status().isNotFound());
    }

    @Test
    void editMaximumPayExistent() throws Exception {
        SalaryScale salaryScale = SalaryScale.builder()
                .id(TestHelpers.getUuid(1))
                .minimumPay(new BigDecimal("1000000.00"))
                .maximumPay(new BigDecimal("10000000.00"))
                .step(new BigDecimal("0.01"))
                .build();

        StringDto newPay = new StringDto("700000000.00");

        SalaryScale changedSalaryScale = SalaryScale.builder()
                .id(TestHelpers.getUuid(2))
                .minimumPay(new BigDecimal("1000000.00"))
                .maximumPay(new BigDecimal("700000000.00"))
                .step(new BigDecimal("0.01"))
                .build();

        when(salaryScaleRepository.findById(TestHelpers.getUuid(1))).thenReturn(Optional.of(salaryScale));
        when(salaryScaleService.editMaximumPay(salaryScale, newPay)).thenReturn(changedSalaryScale);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/internal/salary-scale/{id}/edit-maximum-pay",
                                TestHelpers.getUuid(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPay)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(changedSalaryScale.getDto())));
    }

}
