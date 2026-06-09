package com.meftaul.aurum.repository.projection;

public interface TransactionHistoryAmountByTagAndCustomer extends TransactionHistoryAmountByTag {
    Long getCustomerId();
}
