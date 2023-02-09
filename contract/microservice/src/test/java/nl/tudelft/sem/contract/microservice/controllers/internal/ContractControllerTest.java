package nl.tudelft.sem.contract.microservice.controllers.internal;

import static nl.tudelft.sem.contract.microservice.TestHelpers.getUuid;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.contract.commons.entities.ActionSuccessDto;
import nl.tudelft.sem.contract.commons.entities.ContractModificationDto;
import nl.tudelft.sem.contract.commons.entities.ContractStatus;
import nl.tudelft.sem.contract.commons.entities.ContractType;
import nl.tudelft.sem.contract.microservice.database.entities.Contract;
import nl.tudelft.sem.contract.microservice.database.entities.JobPosition;
import nl.tudelft.sem.contract.microservice.database.entities.contract.ContractInfo;
import nl.tudelft.sem.contract.microservice.database.entities.contract.ContractParties;
import nl.tudelft.sem.contract.microservice.database.entities.contract.ContractTerms;
import nl.tudelft.sem.contract.microservice.database.repositories.ContractRepository;
import nl.tudelft.sem.contract.microservice.exceptions.ContractNotFoundException;
import nl.tudelft.sem.contract.microservice.services.ContractService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@ActiveProfiles(profiles = "testing")
class ContractControllerTest {
    private final transient MockMvc mockMvc;
    private final transient ObjectMapper objectMapper;

    @MockBean
    private transient ContractService contractService;
    @MockBean
    private transient ContractRepository contractRepository;

    @Autowired
    ContractControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper().registerModules(new Jdk8Module(), new JavaTimeModule());
    }

    @Test
    void getNonExistingContract() throws Exception {
        this.mockMvc.perform(get("/internal/contract/{id}", getUuid(1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getExistingContract() throws Exception {
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
                .jobPosition(JobPosition.builder().id(getUuid(3)).name("Job position").build())
                .build();
        when(contractRepository.findById(getUuid(1))).thenReturn(Optional.of(contract));

        this.mockMvc.perform(get("/internal/contract/{id}", getUuid(1)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(contract.getDto())));
    }

    @Test
    void modifyNonExistingContract() throws Exception {
        ContractModificationDto modDto = ContractModificationDto.builder()
                .vacationDays(15)
                .jobPosition("CEO")
                .build();
        this.mockMvc.perform(get("/internal/contract/modify/draft/{id}", getUuid(1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void modifyExistingContract() throws Exception {
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
                .jobPosition(JobPosition.builder().id(getUuid(3)).name("Job position").build())
                .build();

        ContractModificationDto modDto = ContractModificationDto.builder()
                .vacationDays(15)
                .salaryScalePoint(new BigDecimal("0.4"))
                .startDate(LocalDate.of(2022, 1, 15))
                .build();

        // Verify that the modified contract is actually returned
        Contract modifiedContract = Contract.builder()
                .id(getUuid(1))
                .contractParties(new ContractParties(getUuid(2), getUuid(3)))
                .contractInfo(new ContractInfo(ContractType.TEMPORARY, ContractStatus.DRAFT))
                .contractTerms(ContractTerms.builder()
                        .hoursPerWeek(10)
                        .vacationDays(15)
                        .startDate(LocalDate.of(2022, 1, 15))
                        .lastSalaryIncreaseDate(LocalDate.of(2022, 1, 1))
                        .salaryScalePoint(new BigDecimal("0.4"))
                        .build())
                .benefits(List.of())
                .jobPosition(JobPosition.builder().id(getUuid(3)).name("Job position").build())
                .build();

        when(contractRepository.findById(getUuid(1))).thenReturn(Optional.of(contract));
        when(contractService.modifyDraftContract(contract, modDto)).thenReturn(modifiedContract);

        this.mockMvc.perform(put("/internal/contract/{id}", getUuid(1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(modifiedContract.getDto())));
    }

    @Test
    void modifyInvalidContract() throws Exception {
        //noinspection DataFlowIssue
        ContractModificationDto modDto = ContractModificationDto.builder()
                .vacationDays(100) // Invalid
                .salaryScalePoint(new BigDecimal("1.4")) // Invalid
                .startDate(LocalDate.of(2022, 1, 16)) // Invalid
                .build();
        this.mockMvc.perform(put("/internal/contract/{id}", getUuid(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modDto)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void modifyNotDraftContract() throws Exception {
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
                .jobPosition(JobPosition.builder().id(getUuid(3)).name("Job position").build())
                .build();
        when(contractRepository.findById(getUuid(1))).thenReturn(Optional.of(contract));

        ContractModificationDto modDto = ContractModificationDto.builder()
                .vacationDays(15)
                .salaryScalePoint(new BigDecimal("0.4"))
                .startDate(LocalDate.of(2022, 1, 15))
                .build();
        this.mockMvc.perform(put("/internal/contract/{id}", getUuid(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void terminateNonExistingContract() throws Exception {
        doThrow(new ContractNotFoundException()).when(contractService).terminateContract(getUuid(1));
        this.mockMvc.perform(delete("/internal/contract/terminate/{id}", getUuid(1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNonExistingContract() throws Exception {
        this.mockMvc.perform(delete("/internal/contract/{id}", getUuid(1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void terminateExistingContract() throws Exception {
        doNothing().when(contractService).terminateContract(getUuid(1));
        this.mockMvc.perform(delete("/internal/contract/terminate/{id}", getUuid(1)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ActionSuccessDto())));
        verify(contractService, times(1)).terminateContract(getUuid(1));
    }

    @Test
    void deleteExistingContract() throws Exception {
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
                .jobPosition(JobPosition.builder().id(getUuid(3)).name("Job position").build())
                .build();
        when(contractRepository.findById(getUuid(1))).thenReturn(Optional.of(contract));

        this.mockMvc.perform(delete("/internal/contract/{id}", getUuid(1)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(contract.getDto())));

        verify(contractRepository, times(1)).deleteById(getUuid(1));
    }

    @Test
    void addContract() throws Exception {
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
                .jobPosition(JobPosition.builder().id(getUuid(3)).name("Job position").build())
                .build();
        when(contractService.addContract(any())).thenReturn(contract);

        this.mockMvc.perform(post("/internal/contract", getUuid(1))
                        .content(objectMapper.writeValueAsString(contract.getDto()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(contract.getDto())));

        verify(contractService, times(1)).addContract(contract.getDto());
    }
}
