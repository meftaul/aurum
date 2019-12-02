package com.meftaul.aurum.service.dto;

import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.enumeration.VoucherStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TransactionDto {

    @NotNull
    private TransactionHistory transactionHistory;
    @NotNull
    private Boolean deliveryStatus;
    private VoucherStatus voucherStatus;

    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(TransactionHistory transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public Boolean getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Boolean deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public VoucherStatus getVoucherStatus() {
        return voucherStatus;
    }

    public void setVoucherStatus(VoucherStatus voucherStatus) {
        this.voucherStatus = voucherStatus;
    }
}
