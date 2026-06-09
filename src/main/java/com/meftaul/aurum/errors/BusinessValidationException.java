package com.meftaul.aurum.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Business-rule validation failure raised from the service layer. Mirrors the wire behaviour of the
 * web layer's {@code BadRequestAlertException} (HTTP 400 + error alert parameters) but lives in a
 * neutral {@code errors} package so services do not depend on the web layer.
 */
public class BusinessValidationException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private static final URI DEFAULT_TYPE = URI.create("http://admin.maitreegoldbd.com/problem/problem-with-message");

    private final String entityName;

    private final String errorKey;

    public BusinessValidationException(String defaultMessage, String entityName, String errorKey) {
        super(DEFAULT_TYPE, defaultMessage, Status.BAD_REQUEST, null, null, null, getAlertParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }
}
