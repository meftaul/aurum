package com.meftaul.aurum.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.meftaul.aurum.web.rest.TestUtil;

public class KaratTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Karat.class);
        Karat karat1 = new Karat();
        karat1.setId(1L);
        Karat karat2 = new Karat();
        karat2.setId(karat1.getId());
        assertThat(karat1).isEqualTo(karat2);
        karat2.setId(2L);
        assertThat(karat1).isNotEqualTo(karat2);
        karat1.setId(null);
        assertThat(karat1).isNotEqualTo(karat2);
    }
}
