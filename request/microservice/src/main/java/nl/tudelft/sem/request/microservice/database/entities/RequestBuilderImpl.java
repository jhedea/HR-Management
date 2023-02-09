package nl.tudelft.sem.request.microservice.database.entities;

final class RequestBuilderImpl extends RequestBuilder<GeneralRequest, RequestBuilderImpl> {
    RequestBuilderImpl() {
    }

    protected RequestBuilderImpl self() {
        return this;
    }

    public GeneralRequest build() {
        return new GeneralRequest(this);
    }
}
