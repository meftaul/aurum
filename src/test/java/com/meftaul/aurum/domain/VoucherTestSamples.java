package com.meftaul.aurum.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VoucherTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Voucher getVoucherSample1() {
        return new Voucher().id(1L).voucherNo("voucherNo1").customerId(1L).addedBy("addedBy1").boxNumber("boxNumber1");
    }

    public static Voucher getVoucherSample2() {
        return new Voucher().id(2L).voucherNo("voucherNo2").customerId(2L).addedBy("addedBy2").boxNumber("boxNumber2");
    }

    public static Voucher getVoucherRandomSampleGenerator() {
        return new Voucher()
            .id(longCount.incrementAndGet())
            .voucherNo(UUID.randomUUID().toString())
            .customerId(longCount.incrementAndGet())
            .addedBy(UUID.randomUUID().toString())
            .boxNumber(UUID.randomUUID().toString());
    }
}
