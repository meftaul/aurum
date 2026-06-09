package com.meftaul.aurum.domain;

import static com.meftaul.aurum.domain.AurumServiceTestSamples.*;
import static com.meftaul.aurum.domain.VoucherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.meftaul.aurum.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AurumServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AurumService.class);
        AurumService aurumService1 = getAurumServiceSample1();
        AurumService aurumService2 = new AurumService();
        assertThat(aurumService1).isNotEqualTo(aurumService2);

        aurumService2.setId(aurumService1.getId());
        assertThat(aurumService1).isEqualTo(aurumService2);

        aurumService2 = getAurumServiceSample2();
        assertThat(aurumService1).isNotEqualTo(aurumService2);
    }

    @Test
    void voucherTest() {
        AurumService aurumService = getAurumServiceRandomSampleGenerator();
        Voucher voucherBack = getVoucherRandomSampleGenerator();

        aurumService.setVoucher(voucherBack);
        assertThat(aurumService.getVoucher()).isEqualTo(voucherBack);

        aurumService.voucher(null);
        assertThat(aurumService.getVoucher()).isNull();
    }
}
