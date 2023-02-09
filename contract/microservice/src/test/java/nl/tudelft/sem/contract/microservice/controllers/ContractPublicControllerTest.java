package nl.tudelft.sem.contract.microservice.controllers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import nl.tudelft.sem.contract.commons.entities.ContractModificationDto;
import nl.tudelft.sem.contract.commons.entities.ContractStatus;
import nl.tudelft.sem.contract.microservice.database.entities.Contract;
import nl.tudelft.sem.contract.microservice.database.entities.contract.ContractInfo;
import nl.tudelft.sem.contract.microservice.database.entities.contract.ContractParties;
import nl.tudelft.sem.contract.microservice.database.repositories.ContractRepository;
import nl.tudelft.sem.contract.microservice.exceptions.ActionNotAllowedException;
import nl.tudelft.sem.contract.microservice.exceptions.ContractNotFoundException;
import nl.tudelft.sem.contract.microservice.services.RoleAuthenticationService;
import nl.tudelft.sem.user.commons.entities.utils.Role;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@ActiveProfiles(profiles = "testing")
public class ContractPublicControllerTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private RoleAuthenticationService authenticationService;

    @InjectMocks
    private ContractPublicController controller;

    @Test
    public void testGetContract_ContractNotFound() {
        UUID contractId = UUID.randomUUID();
        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        assertThrows(ContractNotFoundException.class, () -> controller.getContract(contractId));
    }

    @Test
    public void testGetContract_throwsException_userIsNotEmployeeOrEmployer() {
        Contract contract = new Contract();

        contract.setContractParties(new ContractParties());
        contract.getContractParties().setEmployeeId(UUID.randomUUID());
        contract.getContractParties().setEmployerId(UUID.randomUUID());

        UUID contractId = UUID.randomUUID();
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(authenticationService.getUserId()).thenReturn(UUID.randomUUID());
        when(authenticationService.hasAtLeastRole(Role.HR)).thenReturn(false);

        assertThrows(ActionNotAllowedException.class, () -> controller.getContract(contractId));
    }

    @Test
    public void testModifyDraftContract_userNotAllowed() {
        UUID contractId = UUID.randomUUID();
        ContractModificationDto mod = new ContractModificationDto();
        when(authenticationService.hasAtLeastRole(Role.HR)).thenReturn(false);

        assertThrows(ActionNotAllowedException.class, () -> controller.modifyDraftContract(contractId, mod));
    }

    @Test
    public void testModifyDraftContract_contractNotDraft() {
        Contract contract = new Contract();
        contract.setContractInfo(new ContractInfo());
        contract.getContractInfo().setStatus(ContractStatus.ACTIVE);

        UUID contractId = UUID.randomUUID();
        when(authenticationService.hasAtLeastRole(Role.HR)).thenReturn(true);
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));

        ContractModificationDto mod = new ContractModificationDto();
        assertThrows(ActionNotAllowedException.class, () -> controller.modifyDraftContract(contractId, mod));
    }

    @Test
    public void testModifyDraftContract_contractNotFound() {
        // arrange
        UUID contractId = UUID.randomUUID();
        ContractModificationDto mod = new ContractModificationDto();
        when(authenticationService.hasAtLeastRole(Role.HR)).thenReturn(true);
        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        // act and assert
        assertThrows(ContractNotFoundException.class, () -> controller.modifyDraftContract(contractId, mod));
    }

}
