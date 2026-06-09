package com.meftaul.aurum.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

/**
 * Thrown when a voucher cannot be located. Lives in a neutral {@code errors} package (not the web
 * layer) so it can be thrown from the service layer without violating the layering architecture rule.
 */
public class VoucherNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private static final URI VOUCHER_NOT_FOUND_TYPE = URI.create("http://admin.maitreegoldbd.com/problem/voucher-not-found");

    public VoucherNotFoundException() {
        super(VOUCHER_NOT_FOUND_TYPE, "Voucher Not Found", Status.BAD_REQUEST);
    }
}
