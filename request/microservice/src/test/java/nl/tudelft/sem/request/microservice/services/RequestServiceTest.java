package nl.tudelft.sem.request.microservice.services;

import static nl.tudelft.sem.request.microservice.TestHelpers.getUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import nl.tudelft.sem.contract.client.ContractClient;
import nl.tudelft.sem.contract.client.ContractData;
import nl.tudelft.sem.contract.commons.entities.ContractDto;
import nl.tudelft.sem.request.commons.entities.RequestStatus;
import nl.tudelft.sem.request.commons.entities.RequestType;
import nl.tudelft.sem.request.microservice.database.entities.Request;
import nl.tudelft.sem.request.microservice.database.repositories.RequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;
    @Mock
    private ContractClient contractClient;
    @Mock
    private ContractData contractData;
    @InjectMocks
    private RequestService requestService;

    @Test
    void approveRequest() {
        Request request = Request.builder()
                .id(getUuid(1))
                .contractId(getUuid(2))
                .status(RequestStatus.OPEN)
                .requestType(RequestType.GENERAL)
                .build();

        when(requestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Request result = requestService.approveRequest(request);
        verify(requestRepository, times(1)).save(any());
        assertEquals(result.getId(), request.getId());
        assertEquals(result.getStatus(), RequestStatus.APPROVED);
    }

    @Test
    void approveVacationRequest()  {

        Request request = Request.builder()
                .id(getUuid(1))
                .contractId(getUuid(2))
                .status(RequestStatus.OPEN)
                .requestType(RequestType.VACATION)
                .startDate(LocalDateTime.of(2022, 1, 1, 1, 1))
                .numberOfDays(5)
                .build();

        ContractDto contractDto = ContractDto.builder()
                .vacationDays(15)
                .salaryScalePoint(BigDecimal.valueOf(0.1))
                .build();


        when(contractClient.contract()).thenReturn(contractData);
        when(contractData.getContract(request.getContractId())).thenReturn(CompletableFuture.completedFuture(contractDto));
        when(requestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Request result = requestService.approveRequest(request);
        verify(requestRepository, times(1)).save(any());
        assertEquals(result.getId(), request.getId());
        assertEquals(result.getStatus(), RequestStatus.APPROVED);
    }

    @Test
    void insufficientDays()  {

        Request request = Request.builder()
                .id(getUuid(1))
                .contractId(getUuid(2))
                .status(RequestStatus.OPEN)
                .requestType(RequestType.VACATION)
                .startDate(LocalDateTime.of(2022, 1, 1, 1, 1))
                .numberOfDays(20)
                .build();

        ContractDto contractDto = ContractDto.builder()
                .vacationDays(15)
                .salaryScalePoint(BigDecimal.valueOf(0.1))
                .build();


        when(contractClient.contract()).thenReturn(contractData);
        when(contractData.getContract(request.getContractId())).thenReturn(CompletableFuture.completedFuture(contractDto));
        when(requestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Request result = requestService.approveRequest(request);
        verify(requestRepository, times(1)).save(any());
        assertEquals(result.getId(), request.getId());
        assertEquals(result.getResponseBody(), "You don't have enough remaining vacation days.");
        assertEquals(result.getStatus(), RequestStatus.REJECTED);
    }

    @Test
    void approveLeave() {
        Request request = Request.builder()
                .id(getUuid(1))
                .contractId(getUuid(2))
                .status(RequestStatus.OPEN)
                .requestType(RequestType.LEAVE)
                .build();

        when(requestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Request result = requestService.approveRequest(request);
        verify(requestRepository, times(1)).save(any());
        assertEquals(result.getId(), request.getId());
        assertEquals(result.getStatus(), RequestStatus.APPROVED);
    }

    @Test
    void rejectRequest() {
        Request request = Request.builder()
                .id(getUuid(1))
                .contractId(getUuid(2))
                .status(RequestStatus.OPEN)
                .requestType(RequestType.GENERAL)
                .build();

        when(requestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Request result = requestService.rejectRequest(request, "We have denied the request because of X");
        verify(requestRepository, times(1)).save(any());
        assertEquals(result.getId(), request.getId());
        assertEquals(result.getStatus(), RequestStatus.REJECTED);
        assertEquals(result.getResponseBody(), "We have denied the request because of X");
    }

    @Test
    void approveTerminationRequest() {
        Request request = Request.builder()
                .id(getUuid(1))
                .contractId(getUuid(2))
                .status(RequestStatus.OPEN)
                .requestType(RequestType.TERMINATION)
                .build();

        when(requestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Request result = requestService.approveRequest(request);
        verify(requestRepository, times(1)).save(any());
        assertEquals(result.getId(), request.getId());
        assertEquals(result.getStatus(), RequestStatus.APPROVED);
    }
}
