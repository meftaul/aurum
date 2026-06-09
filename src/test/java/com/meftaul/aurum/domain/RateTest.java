package com.meftaul.aurum.domain;

import static com.meftaul.aurum.domain.RateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.meftaul.aurum.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rate.class);
        Rate rate1 = getRateSample1();
        Rate rate2 = new Rate();
        assertThat(rate1).isNotEqualTo(rate2);

        rate2.setId(rate1.getId());
        assertThat(rate1).isEqualTo(rate2);

        rate2 = getRateSample2();
        assertThat(rate1).isNotEqualTo(rate2);
    }
}
