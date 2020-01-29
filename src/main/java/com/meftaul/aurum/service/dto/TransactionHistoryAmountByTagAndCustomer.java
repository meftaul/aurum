package com.meftaul.aurum.service.dto;

public interface TransactionHistoryAmountByTagAndCustomer extends TransactionHistoryAmountByTag {
    Long getCustomerId();
}
