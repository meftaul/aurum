package com.meftaul.aurum.service.dto;

import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.Voucher;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class CustomVoucherDto {
    private Voucher voucher;

    @Min(value = 0)
    private BigDecimal paidAmount;

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }
}
