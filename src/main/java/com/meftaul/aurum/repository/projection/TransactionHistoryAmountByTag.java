package com.meftaul.aurum.repository.projection;

import java.math.BigDecimal;

public interface TransactionHistoryAmountByTag {
    String getTag();
    BigDecimal getTotalAmount();
}
