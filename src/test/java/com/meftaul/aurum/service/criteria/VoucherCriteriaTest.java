package com.meftaul.aurum.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VoucherCriteriaTest {

    @Test
    void newVoucherCriteriaHasAllFiltersNullTest() {
        var voucherCriteria = new VoucherCriteria();
        assertThat(voucherCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void voucherCriteriaFluentMethodsCreatesFiltersTest() {
        var voucherCriteria = new VoucherCriteria();

        setAllFilters(voucherCriteria);

        assertThat(voucherCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void voucherCriteriaCopyCreatesNullFilterTest() {
        var voucherCriteria = new VoucherCriteria();
        var copy = voucherCriteria.copy();

        assertThat(voucherCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(voucherCriteria)
        );
    }

    @Test
    void voucherCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var voucherCriteria = new VoucherCriteria();
        setAllFilters(voucherCriteria);

        var copy = voucherCriteria.copy();

        assertThat(voucherCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(voucherCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var voucherCriteria = new VoucherCriteria();

        assertThat(voucherCriteria).hasToString("VoucherCriteria{}");
    }

    private static void setAllFilters(VoucherCriteria voucherCriteria) {
        voucherCriteria.id();
        voucherCriteria.voucherNo();
        voucherCriteria.customerId();
        voucherCriteria.calculatedTotalAmount();
        voucherCriteria.vat();
        voucherCriteria.disountAmount();
        voucherCriteria.status();
        voucherCriteria.totalPayableAmount();
        voucherCriteria.dateCreated();
        voucherCriteria.addedBy();
        voucherCriteria.boxNumber();
        voucherCriteria.deliveryDate();
        voucherCriteria.deliveryStatus();
        voucherCriteria.aurumServiceId();
        voucherCriteria.distinct();
    }

    private static Condition<VoucherCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getVoucherNo()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getCalculatedTotalAmount()) &&
                condition.apply(criteria.getVat()) &&
                condition.apply(criteria.getDisountAmount()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getTotalPayableAmount()) &&
                condition.apply(criteria.getDateCreated()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getBoxNumber()) &&
                condition.apply(criteria.getDeliveryDate()) &&
                condition.apply(criteria.getDeliveryStatus()) &&
                condition.apply(criteria.getAurumServiceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<VoucherCriteria> copyFiltersAre(VoucherCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getVoucherNo(), copy.getVoucherNo()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getCalculatedTotalAmount(), copy.getCalculatedTotalAmount()) &&
                condition.apply(criteria.getVat(), copy.getVat()) &&
                condition.apply(criteria.getDisountAmount(), copy.getDisountAmount()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getTotalPayableAmount(), copy.getTotalPayableAmount()) &&
                condition.apply(criteria.getDateCreated(), copy.getDateCreated()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getBoxNumber(), copy.getBoxNumber()) &&
                condition.apply(criteria.getDeliveryDate(), copy.getDeliveryDate()) &&
                condition.apply(criteria.getDeliveryStatus(), copy.getDeliveryStatus()) &&
                condition.apply(criteria.getAurumServiceId(), copy.getAurumServiceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
