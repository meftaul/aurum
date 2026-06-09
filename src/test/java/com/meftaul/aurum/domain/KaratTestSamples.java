package com.meftaul.aurum.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class KaratTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Karat getKaratSample1() {
        return new Karat().id(1L).karatType("karatType1");
    }

    public static Karat getKaratSample2() {
        return new Karat().id(2L).karatType("karatType2");
    }

    public static Karat getKaratRandomSampleGenerator() {
        return new Karat().id(longCount.incrementAndGet()).karatType(UUID.randomUUID().toString());
    }
}
