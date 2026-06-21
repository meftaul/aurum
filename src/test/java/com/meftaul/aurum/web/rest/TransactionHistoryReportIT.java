package com.meftaul.aurum.web.rest;

import static org.assertj.core.api.Assertions.assertThat;

import com.meftaul.aurum.IntegrationTest;
import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.domain.enumeration.TransactionStatus;
import com.meftaul.aurum.domain.enumeration.VoucherStatus;
import com.meftaul.aurum.service.TransactionHistoryQueryService;
import com.meftaul.aurum.service.criteria.TransactionHistoryCriteria;
import com.meftaul.aurum.service.dto.TxnReportDto;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

/**
 * Integration test for the aggregated transaction report ({@link TransactionHistoryQueryService#reportByCriteria}).
 * Seeds two vouchers (with services) and the transactions referencing them, then asserts the DB-side aggregation.
 */
@IntegrationTest
@Transactional
class TransactionHistoryReportIT {

    private static final String VOUCHER_PREFIX = "ZZRPT";

    @Autowired
    private EntityManager em;

    @Autowired
    private TransactionHistoryQueryService queryService;

    private static BigDecimal bd(String value) {
        return new BigDecimal(value);
    }

    private Voucher persistVoucher(String voucherNo, String totalPayable) {
        Voucher voucher = new Voucher()
            .voucherNo(voucherNo)
            .calculatedTotalAmount(bd(totalPayable))
            .totalPayableAmount(bd(totalPayable))
            .status(VoucherStatus.DUE)
            .addedBy("report-it");
        em.persist(voucher);
        return voucher;
    }

    private void persistService(Voucher voucher, String serviceType, String amount, String serviceCharge) {
        AurumService service = new AurumService()
            .serviceType(serviceType)
            .amount(bd(amount))
            .serviceCharge(bd(serviceCharge))
            .voucher(voucher);
        em.persist(service);
    }

    private void persistTxn(String voucherNo, String amount, TransactionStatus tag) {
        TransactionHistory txn = new TransactionHistory()
            .voucherNo(voucherNo)
            .amount(bd(amount))
            .dateCreated(Instant.now())
            .tag(tag)
            .customerId(1L)
            .addedBy("report-it");
        em.persist(txn);
    }

    @Test
    void reportByCriteriaAggregatesTagsServicesAndPayable() {
        Voucher v1 = persistVoucher(VOUCHER_PREFIX + "-1", "1000");
        persistService(v1, "X-Ray", "100", "0");
        persistService(v1, "Normal Melting", "200", "20");

        Voucher v2 = persistVoucher(VOUCHER_PREFIX + "-2", "500");
        persistService(v2, "Hallmark", "80", "0");
        persistService(v2, "Calculated Melting", "150", "15");

        persistTxn(VOUCHER_PREFIX + "-1", "700", TransactionStatus.RECEIVE);
        persistTxn(VOUCHER_PREFIX + "-1", "50", TransactionStatus.DISCOUNT);
        persistTxn(VOUCHER_PREFIX + "-1", "30", TransactionStatus.VAT);
        persistTxn(VOUCHER_PREFIX + "-2", "400", TransactionStatus.RECEIVE);
        persistTxn(VOUCHER_PREFIX + "-2", "10", TransactionStatus.REFUND);
        em.flush();

        TransactionHistoryCriteria criteria = new TransactionHistoryCriteria();
        StringFilter voucherFilter = new StringFilter();
        voucherFilter.setContains(VOUCHER_PREFIX);
        criteria.setVoucherNo(voucherFilter);

        TxnReportDto report = queryService.reportByCriteria(criteria);

        // Per-tag counts and totals.
        assertThat(report.getReceivedVoucherCount()).isEqualTo(2L);
        assertThat(report.getTotalReceived()).isEqualByComparingTo(bd("1100"));
        assertThat(report.getDiscountVoucherCount()).isEqualTo(1L);
        assertThat(report.getTotalDiscount()).isEqualByComparingTo(bd("50"));
        assertThat(report.getVatVoucherCount()).isEqualTo(1L);
        assertThat(report.getTotalVat()).isEqualByComparingTo(bd("30"));
        assertThat(report.getRefundVoucherCount()).isEqualTo(1L);
        assertThat(report.getTotalRefund()).isEqualByComparingTo(bd("10"));

        // Distinct vouchers behind the filtered transactions.
        assertThat(report.getTotalVoucherCount()).isEqualTo(2L);

        // Per-service-type breakdown.
        assertThat(report.getxRayAmount()).isEqualByComparingTo(bd("100"));
        assertThat(report.getHallMarkAmount()).isEqualByComparingTo(bd("80"));
        assertThat(report.getNormalMeltingAmount()).isEqualByComparingTo(bd("200"));
        assertThat(report.getNormalMeltingServiceChargeAmount()).isEqualByComparingTo(bd("20"));
        assertThat(report.getCalculatedMeltingAmount()).isEqualByComparingTo(bd("150"));
        assertThat(report.getCalculatedMeltingServiceChargeAmount()).isEqualByComparingTo(bd("15"));

        // Payable + derived due (= payable - received - discount).
        assertThat(report.getTotalPayableAmount()).isEqualByComparingTo(bd("1500"));
        assertThat(report.getDue()).isEqualByComparingTo(bd("350"));
    }

    @Test
    void reportByCriteriaRespectsTagFilter() {
        Voucher v1 = persistVoucher(VOUCHER_PREFIX + "-T1", "1000");
        persistService(v1, "X-Ray", "100", "0");
        persistTxn(VOUCHER_PREFIX + "-T1", "700", TransactionStatus.RECEIVE);
        persistTxn(VOUCHER_PREFIX + "-T1", "50", TransactionStatus.DISCOUNT);
        em.flush();

        // Mirrors the frontend default: tag=RECEIVE constrains every aggregate, so DISCOUNT is excluded.
        TransactionHistoryCriteria criteria = new TransactionHistoryCriteria();
        StringFilter voucherFilter = new StringFilter();
        voucherFilter.setContains(VOUCHER_PREFIX + "-T1");
        criteria.setVoucherNo(voucherFilter);
        TransactionHistoryCriteria.TransactionStatusFilter tagFilter = new TransactionHistoryCriteria.TransactionStatusFilter();
        tagFilter.setEquals(TransactionStatus.RECEIVE);
        criteria.setTag(tagFilter);

        TxnReportDto report = queryService.reportByCriteria(criteria);

        assertThat(report.getReceivedVoucherCount()).isEqualTo(1L);
        assertThat(report.getTotalReceived()).isEqualByComparingTo(bd("700"));
        assertThat(report.getDiscountVoucherCount()).isEqualTo(0L);
        assertThat(report.getTotalDiscount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(report.getTotalVoucherCount()).isEqualTo(1L);
        assertThat(report.getxRayAmount()).isEqualByComparingTo(bd("100"));
        // due = payable(1000) - received(700) - discount(0)
        assertThat(report.getDue()).isEqualByComparingTo(bd("300"));
    }
}
