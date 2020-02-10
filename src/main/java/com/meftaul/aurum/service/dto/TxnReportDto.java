package com.meftaul.aurum.service.dto;

import java.math.BigDecimal;

public class TxnReportDto {
    Long discountVoucherCount = 0l;
    Long receivedVoucherCount = 0l;
    Long vatVoucherCount = 0l;
    Long refundVoucherCount = 0l;
    Long totalVoucherCount = 0l;

    BigDecimal totalDiscount = BigDecimal.ZERO;
    BigDecimal totalReceived = BigDecimal.ZERO;
    BigDecimal totalVat = BigDecimal.ZERO;
    BigDecimal totalRefund = BigDecimal.ZERO;
    BigDecimal totalPayableAmount = BigDecimal.ZERO;
    BigDecimal due = BigDecimal.ZERO;

    public BigDecimal getDue() {
        return due;
    }

    public void setDue(BigDecimal due) {
        this.due = due;
    }

    public Long getTotalVoucherCount() {
        return totalVoucherCount;
    }

    public void setTotalVoucherCount(Long totalVoucherCount) {
        this.totalVoucherCount = totalVoucherCount;
    }

    public BigDecimal getTotalPayableAmount() {
        return totalPayableAmount;
    }

    public void setTotalPayableAmount(BigDecimal totalPayableAmount) {
        this.totalPayableAmount = totalPayableAmount;
    }

    public Long getDiscountVoucherCount() {
        return discountVoucherCount;
    }

    public void setDiscountVoucherCount(Long discountVoucherCount) {
        this.discountVoucherCount = discountVoucherCount;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Long getReceivedVoucherCount() {
        return receivedVoucherCount;
    }

    public void setReceivedVoucherCount(Long receivedVoucherCount) {
        this.receivedVoucherCount = receivedVoucherCount;
    }

    public Long getVatVoucherCount() {
        return vatVoucherCount;
    }

    public void setVatVoucherCount(Long vatVoucherCount) {
        this.vatVoucherCount = vatVoucherCount;
    }

    public Long getRefundVoucherCount() {
        return refundVoucherCount;
    }

    public void setRefundVoucherCount(Long refundVoucherCount) {
        this.refundVoucherCount = refundVoucherCount;
    }

    public BigDecimal getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(BigDecimal totalReceived) {
        this.totalReceived = totalReceived;
    }

    public BigDecimal getTotalVat() {
        return totalVat;
    }

    public void setTotalVat(BigDecimal totalVat) {
        this.totalVat = totalVat;
    }

    public BigDecimal getTotalRefund() {
        return totalRefund;
    }

    public void setTotalRefund(BigDecimal totalRefund) {
        this.totalRefund = totalRefund;
    }
}
