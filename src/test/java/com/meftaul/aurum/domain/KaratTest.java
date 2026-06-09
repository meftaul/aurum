package com.meftaul.aurum.domain;

import static com.meftaul.aurum.domain.KaratTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.meftaul.aurum.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KaratTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Karat.class);
        Karat karat1 = getKaratSample1();
        Karat karat2 = new Karat();
        assertThat(karat1).isNotEqualTo(karat2);

        karat2.setId(karat1.getId());
        assertThat(karat1).isEqualTo(karat2);

        karat2 = getKaratSample2();
        assertThat(karat1).isNotEqualTo(karat2);
    }
}
