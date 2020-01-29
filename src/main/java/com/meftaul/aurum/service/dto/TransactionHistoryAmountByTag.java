package com.meftaul.aurum.service.dto;

import java.math.BigDecimal;

public interface TransactionHistoryAmountByTag {
    String getTag();
    BigDecimal getTotalAmount();
}
