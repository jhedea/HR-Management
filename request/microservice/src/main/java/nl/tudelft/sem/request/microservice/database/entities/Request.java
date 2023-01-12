package nl.tudelft.sem.request.microservice.database.entities;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.tudelft.sem.request.commons.entities.RequestDto;
import nl.tudelft.sem.request.commons.entities.RequestStatus;
import nl.tudelft.sem.request.commons.entities.RequestType;
import nl.tudelft.sem.request.microservice.database.entities.utils.BaseEntity;
import org.modelmapper.ModelMapper;

@SuppressWarnings("com.haulmont.jpb.LombokEqualsAndHashCodeInspection")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Request extends BaseEntity<RequestDto> {

    /**
     * Status of the request.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @NotNull
    @Column(nullable = false)
    private String author;

    private UUID contractId;

    private RequestType requestType;

    private String requestBody;

    @NotNull
    private LocalDateTime requestDate;

    private String responseBody;

    @NotNull
    private LocalDateTime responseDate;

    private LocalDateTime startDate;

    private int numberOfDays;

    protected Request(RequestBuilder<?, ?> b) {
        super(b);
        this.status = b.statusForm;
        this.author = b.authorForm;
        this.contractId = b.contractIdForm;
        this.requestType = b.requestTypeForm;
        this.requestBody = b.requestBodyForm;
        this.requestDate = b.requestDateForm;
        this.responseBody = b.responseBodyForm;
        this.responseDate = b.responseDateForm;
        this.startDate = b.startDateForm;
        this.numberOfDays = b.numberOfDaysForm;
    }

    public static RequestBuilder<?, ?> builder() {
        return new RequestBuilderImpl();
    }

    /**
     * Create a new Request entity.
     *
     * @param requestDto Dto with request details.
     */
    public Request(RequestDto requestDto) {

        this.requestType = requestDto.getRequestType();
        this.requestBody = requestDto.getRequestBody();
        this.requestDate = requestDto.getRequestDate();
        this.contractId = requestDto.getContractId();

        if (requestDto.getRequestType() == RequestType.VACATION || requestDto.getRequestType() == RequestType.LEAVE) {
            this.startDate = requestDto.getStartDate();
            this.numberOfDays = requestDto.getNumberOfDays();

        }

    }

    @Override
    public RequestDto getDto() {
        return new ModelMapper().map(this, RequestDto.class);
    }

    public abstract static class RequestBuilder<C extends Request, B extends RequestBuilder<C, B>>
            extends BaseEntityBuilder<RequestDto, C, B> {
        private transient UUID contractIdForm;
        private transient RequestType requestTypeForm;
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

        public B requestType(RequestType requestType) {
            this.requestTypeForm = requestType;
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
    }

    private static final class RequestBuilderImpl extends RequestBuilder<Request, RequestBuilderImpl> {
        private RequestBuilderImpl() {
        }

        protected RequestBuilderImpl self() {
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }
}
