package com.meftaul.aurum.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.meftaul.aurum.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AurumServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AurumService.class);
        AurumService aurumService1 = new AurumService();
        aurumService1.setId(1L);
        AurumService aurumService2 = new AurumService();
        aurumService2.setId(aurumService1.getId());
        assertThat(aurumService1).isEqualTo(aurumService2);
        aurumService2.setId(2L);
        assertThat(aurumService1).isNotEqualTo(aurumService2);
        aurumService1.setId(null);
        assertThat(aurumService1).isNotEqualTo(aurumService2);
    }
}
