package com.meftaul.aurum.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AurumServiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AurumService getAurumServiceSample1() {
        return new AurumService()
            .id(1L)
            .serviceType("serviceType1")
            .itemName("itemName1")
            .quantity(1)
            .serviceName("serviceName1")
            .karatType("karatType1")
            .expectedKaratType("expectedKaratType1")
            .hallMarkedText("hallMarkedText1")
            .weightOfFreeCheck("weightOfFreeCheck1");
    }

    public static AurumService getAurumServiceSample2() {
        return new AurumService()
            .id(2L)
            .serviceType("serviceType2")
            .itemName("itemName2")
            .quantity(2)
            .serviceName("serviceName2")
            .karatType("karatType2")
            .expectedKaratType("expectedKaratType2")
            .hallMarkedText("hallMarkedText2")
            .weightOfFreeCheck("weightOfFreeCheck2");
    }

    public static AurumService getAurumServiceRandomSampleGenerator() {
        return new AurumService()
            .id(longCount.incrementAndGet())
            .serviceType(UUID.randomUUID().toString())
            .itemName(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet())
            .serviceName(UUID.randomUUID().toString())
            .karatType(UUID.randomUUID().toString())
            .expectedKaratType(UUID.randomUUID().toString())
            .hallMarkedText(UUID.randomUUID().toString())
            .weightOfFreeCheck(UUID.randomUUID().toString());
    }
}
