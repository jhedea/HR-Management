package nl.tudelft.sem.request.microservice.database.entities;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import nl.tudelft.sem.request.commons.entities.RequestDto;
import nl.tudelft.sem.request.commons.entities.RequestStatus;
import nl.tudelft.sem.request.microservice.database.entities.utils.BaseEntity;

public abstract class RequestBuilder<C extends GeneralRequest, B extends RequestBuilder<C, B>>
        extends BaseEntity.BaseEntityBuilder<RequestDto, C, B> {
    private transient UUID contractIdForm;
    private transient @NotNull RequestStatus statusForm;
    private transient @NotNull String authorForm;
    private transient String requestBodyForm;
    private transient @NotNull LocalDateTime requestDateForm;
    private transient String responseBodyForm;
    private transient LocalDateTime responseDateForm;
    private transient LocalDateTime startDateForm;
    private transient int numberOfDaysForm;

    public B status(@NotNull RequestStatus status) {
        this.statusForm = status;
        return self();
    }

    public B contractId(UUID contractId) {
        this.contractIdForm = contractId;
        return self();
    }

    public B author(@NotNull String author) {
        this.authorForm = author;
        return self();
    }

    public B requestBody(String requestBody) {
        this.requestBodyForm = requestBody;
        return self();
    }

    public B requestDate(@NotNull LocalDateTime requestDate) {
        this.requestDateForm = requestDate;
        return self();
    }

    public B responseBody(String responseBody) {
        this.responseBodyForm = responseBody;
        return self();
    }

    public B responseDate(LocalDateTime responseDate) {
        this.responseDateForm = responseDate;
        return self();
    }

    public B startDate(LocalDateTime startDate) {
        this.startDateForm = startDate;
        return self();
    }

    public B numberOfDays(int numberOfDays) {
        this.numberOfDaysForm = numberOfDays;
        return self();
    }

    protected abstract B self();

    public abstract C build();

    public UUID getContractIdForm() {
        return this.contractIdForm;
    }

    public @NotNull RequestStatus getStatusForm() {
        return this.statusForm;
    }

    public @NotNull String getAuthorForm() {
        return this.authorForm;
    }

    public String getRequestBodyForm() {
        return this.requestBodyForm;
    }

    public @NotNull LocalDateTime getRequestDateForm() {
        return this.requestDateForm;
    }

    public String getResponseBodyForm() {
        return this.responseBodyForm;
    }

    public LocalDateTime getResponseDateForm() {
        return this.responseDateForm;
    }

    public LocalDateTime getStartDateForm() {
        return this.startDateForm;
    }

    public int getNumberOfDaysForm() {
        return this.numberOfDaysForm;
    }
}
