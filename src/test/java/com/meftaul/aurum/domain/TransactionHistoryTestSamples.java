package com.meftaul.aurum.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TransactionHistory getTransactionHistorySample1() {
        return new TransactionHistory().id(1L).voucherNo("voucherNo1").customerId(1L).addedBy("addedBy1");
    }

    public static TransactionHistory getTransactionHistorySample2() {
        return new TransactionHistory().id(2L).voucherNo("voucherNo2").customerId(2L).addedBy("addedBy2");
    }

    public static TransactionHistory getTransactionHistoryRandomSampleGenerator() {
        return new TransactionHistory()
            .id(longCount.incrementAndGet())
            .voucherNo(UUID.randomUUID().toString())
            .customerId(longCount.incrementAndGet())
            .addedBy(UUID.randomUUID().toString());
    }
}
