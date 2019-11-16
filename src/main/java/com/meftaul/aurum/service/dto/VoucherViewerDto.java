package com.meftaul.aurum.service.dto;

import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.Voucher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VoucherViewerDto {
    private Voucher voucherInfo;
    private List<TransactionHistory> txnHistory = new ArrayList<TransactionHistory>();
    private BigDecimal totalAmount;
    private BigDecimal dueAmount;

    public Voucher getVoucherInfo() {
        return voucherInfo;
    }

    public void setVoucherInfo(Voucher voucherInfo) {
        this.voucherInfo = voucherInfo;
    }

    public List<TransactionHistory> getTxnHistory() {
        return txnHistory;
    }

    public void setTxnHistory(List<TransactionHistory> txnHistory) {
        this.txnHistory = txnHistory;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }
}
