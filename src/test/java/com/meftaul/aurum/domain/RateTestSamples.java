package com.meftaul.aurum.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Rate getRateSample1() {
        return new Rate().id(1L).rateType("rateType1");
    }

    public static Rate getRateSample2() {
        return new Rate().id(2L).rateType("rateType2");
    }

    public static Rate getRateRandomSampleGenerator() {
        return new Rate().id(longCount.incrementAndGet()).rateType(UUID.randomUUID().toString());
    }
}
