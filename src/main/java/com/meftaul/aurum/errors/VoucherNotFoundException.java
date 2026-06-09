package com.meftaul.aurum.errors;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import tech.jhipster.web.rest.errors.ProblemDetailWithCause.ProblemDetailWithCauseBuilder;

/**
 * Thrown when a voucher cannot be located. Lives in a neutral {@code errors} package (not the web
 * layer) so it can be thrown from the service layer without violating the layering architecture rule.
 */
@SuppressWarnings("java:S110")
public class VoucherNotFoundException extends ErrorResponseException {

    private static final long serialVersionUID = 1L;

    private static final URI VOUCHER_NOT_FOUND_TYPE = URI.create("http://admin.maitreegoldbd.com/problem/voucher-not-found");

    public VoucherNotFoundException() {
        super(
            HttpStatus.BAD_REQUEST,
            ProblemDetailWithCauseBuilder.instance()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withType(VOUCHER_NOT_FOUND_TYPE)
                .withTitle("Voucher Not Found")
                .build(),
            null
        );
    }
}
