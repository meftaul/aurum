package com.meftaul.aurum.domain;

import static com.meftaul.aurum.domain.AurumServiceTestSamples.*;
import static com.meftaul.aurum.domain.VoucherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.meftaul.aurum.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VoucherTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Voucher.class);
        Voucher voucher1 = getVoucherSample1();
        Voucher voucher2 = new Voucher();
        assertThat(voucher1).isNotEqualTo(voucher2);

        voucher2.setId(voucher1.getId());
        assertThat(voucher1).isEqualTo(voucher2);

        voucher2 = getVoucherSample2();
        assertThat(voucher1).isNotEqualTo(voucher2);
    }

    @Test
    void aurumServiceTest() {
        Voucher voucher = getVoucherRandomSampleGenerator();
        AurumService aurumServiceBack = getAurumServiceRandomSampleGenerator();

        voucher.addAurumService(aurumServiceBack);
        assertThat(voucher.getAurumServices()).containsOnly(aurumServiceBack);
        assertThat(aurumServiceBack.getVoucher()).isEqualTo(voucher);

        voucher.removeAurumService(aurumServiceBack);
        assertThat(voucher.getAurumServices()).doesNotContain(aurumServiceBack);
        assertThat(aurumServiceBack.getVoucher()).isNull();

        voucher.aurumServices(new HashSet<>(Set.of(aurumServiceBack)));
        assertThat(voucher.getAurumServices()).containsOnly(aurumServiceBack);
        assertThat(aurumServiceBack.getVoucher()).isEqualTo(voucher);

        voucher.setAurumServices(new HashSet<>());
        assertThat(voucher.getAurumServices()).doesNotContain(aurumServiceBack);
        assertThat(aurumServiceBack.getVoucher()).isNull();
    }
}
