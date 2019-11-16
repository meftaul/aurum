package com.meftaul.aurum.service.dto;

import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.Voucher;

import java.math.BigDecimal;

public class CustomVoucherDto {
    private Voucher voucher;
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
