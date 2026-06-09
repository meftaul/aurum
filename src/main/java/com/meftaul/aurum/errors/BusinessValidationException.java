package com.meftaul.aurum.errors;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import tech.jhipster.web.rest.errors.ProblemDetailWithCause.ProblemDetailWithCauseBuilder;

/**
 * Business-rule validation failure raised from the service layer. Mirrors the wire behaviour of the
 * web layer's {@code BadRequestAlertException} (HTTP 400 + error alert properties) but lives in a
 * neutral {@code errors} package so services do not depend on the web layer.
 */
@SuppressWarnings("java:S110")
public class BusinessValidationException extends ErrorResponseException {

    private static final long serialVersionUID = 1L;

    private static final URI DEFAULT_TYPE = URI.create("http://admin.maitreegoldbd.com/problem/problem-with-message");

    private final String entityName;

    private final String errorKey;

    public BusinessValidationException(String defaultMessage, String entityName, String errorKey) {
        super(
            HttpStatus.BAD_REQUEST,
            ProblemDetailWithCauseBuilder.instance()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withType(DEFAULT_TYPE)
                .withTitle(defaultMessage)
                .withProperty("message", "error." + errorKey)
                .withProperty("params", entityName)
                .build(),
            null
        );
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
