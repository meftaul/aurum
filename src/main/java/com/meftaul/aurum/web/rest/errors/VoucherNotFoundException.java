package com.meftaul.aurum.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class VoucherNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public VoucherNotFoundException() {
        super(ErrorConstants.VOUCHER_NOT_FOUND_TYPE, "Voucher Not Found", Status.BAD_REQUEST);
    }
}
